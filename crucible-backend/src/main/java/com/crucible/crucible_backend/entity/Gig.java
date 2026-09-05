package com.crucible.crucible_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "gigs")
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GigType type;

    private BigDecimal budget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // --- Blockchain Integration ---
    @Column(name = "escrow_contract_address", unique = true)
    private String escrowContractAddress;

    // --- Constructors ---
    public Gig() {
    }

    public Gig(String title, String description, GigType type, BigDecimal budget, Project project) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.budget = budget;
        this.project = project;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GigType getType() {
        return type;
    }

    public void setType(GigType type) {
        this.type = type;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    // Safe alias for financial services expecting getTotalBudget()
    public BigDecimal getTotalBudget() {
        return budget;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getEscrowContractAddress() {
        return escrowContractAddress;
    }

    public void setEscrowContractAddress(String escrowContractAddress) {
        this.escrowContractAddress = escrowContractAddress;
    }

    /**
     * Converts the BigDecimal budget to BigInteger cents.
     * Solidity uint256 requires integer math to prevent floating-point rounding errors.
     */
    public BigInteger getBudgetInCents() {
        if (this.budget == null) {
            return BigInteger.ZERO;
        }
        return this.budget.multiply(new BigDecimal("100")).toBigInteger();
    }
}