package com.crucible.crucible_backend.strategy;

import com.crucible.crucible_backend.service.SandboxOrchestratorService;
import org.springframework.stereotype.Component;

@Component
public class BackendApiJudgingStrategy implements JudgingStrategy {

    private final SandboxOrchestratorService sandboxOrchestrator;

    public BackendApiJudgingStrategy(SandboxOrchestratorService sandboxOrchestrator) {
        this.sandboxOrchestrator = sandboxOrchestrator;
    }

    @Override
    public boolean supports(String gigType) {
        return "BACKEND_API".equalsIgnoreCase(gigType);
    }

    @Override
    public Double evaluate(String deliverableReference) {
        Integer exitCode = sandboxOrchestrator.runAutomatedTests(deliverableReference);

        // Return 100.0 for success, 0.0 for failure to match your Double field
        return (exitCode == 0) ? 100.0 : 0.0;
    }
}