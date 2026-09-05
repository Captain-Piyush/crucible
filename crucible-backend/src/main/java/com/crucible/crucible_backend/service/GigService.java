package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.dto.GigCreateRequest;
import com.crucible.crucible_backend.dto.GigResponse;
import com.crucible.crucible_backend.entity.Gig;
import com.crucible.crucible_backend.entity.Project;
import com.crucible.crucible_backend.repository.GigRepository;
import com.crucible.crucible_backend.repository.ProjectRepository;
import com.crucible.escrow.service.EscrowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class GigService {

    private final GigRepository gigRepository;
    private final ProjectRepository projectRepository;
    private final EscrowService escrowService;

    public GigService(GigRepository gigRepository, ProjectRepository projectRepository, EscrowService escrowService) {
        this.gigRepository = gigRepository;
        this.projectRepository = projectRepository;
        this.escrowService = escrowService;
    }

    @Transactional
    public GigResponse createGig(GigCreateRequest request) {
        // 1. Fetch the parent project
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        BigDecimal finalBudget = request.getBudget();

        // 2. The Heuristic logic: Auto-calculate if no budget was provided
        if (finalBudget == null) {
            finalBudget = calculateDefaultBudget(project.getTotalBudget(), request.getGigType());
        }

        // 3. Map to Entity and save FIRST to generate the relational ID
        Gig gig = new Gig();
        gig.setTitle(request.getTitle());
        gig.setDescription(request.getDescription());
        gig.setType(request.getGigType());
        gig.setBudget(finalBudget);
        gig.setProject(project);

        Gig savedGig = gigRepository.save(gig);

        // 4. Trigger Web3j Oracle to spawn the smart contract
        System.out.println("Triggering blockchain deployment for Gig ID: " + savedGig.getId());
        String escrowAddress = escrowService.createEscrowRecord(
                savedGig.getId(),
                savedGig.getBudgetInCents()
        );

        // 5. Link the immutable contract address back to the Postgres row
        savedGig.setEscrowContractAddress(escrowAddress);
        savedGig = gigRepository.save(savedGig);

        // 6. Map back to Response DTO
        GigResponse response = new GigResponse();
        response.setId(savedGig.getId());
        response.setTitle(savedGig.getTitle());
        response.setDescription(savedGig.getDescription());
        response.setGigType(savedGig.getType());
        response.setBudget(savedGig.getBudget());
        response.setProjectId(project.getId());

        // Note: Add this field to your GigResponse DTO if you want to return it to the frontend!
        // response.setEscrowContractAddress(savedGig.getEscrowContractAddress());

        return response;
    }

    private BigDecimal calculateDefaultBudget(BigDecimal totalProjectBudget, com.crucible.crucible_backend.entity.GigType type) {
        double multiplier = switch (type) {
            case BACKEND_API -> 0.40;
            case FRONTEND -> 0.35;
            case DATABASE -> 0.25;
            default -> 0.20;
        };

        return totalProjectBudget.multiply(BigDecimal.valueOf(multiplier));
    }
}