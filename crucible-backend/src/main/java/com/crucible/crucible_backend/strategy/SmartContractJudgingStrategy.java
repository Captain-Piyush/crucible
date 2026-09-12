package com.crucible.crucible_backend.strategy;

import org.springframework.stereotype.Component;

@Component
public class SmartContractJudgingStrategy implements JudgingStrategy {

    @Override
    public boolean supports(String gigType) {
        return "SMART_CONTRACT".equalsIgnoreCase(gigType);
    }

    @Override
    public Double evaluate(String deliverableReference) {
        try {
            ProcessBuilder pb = new ProcessBuilder("npx", "hardhat", "test", deliverableReference);
            Process process = pb.start();
            int exitCode = process.waitFor();

            // Returns a perfect score if all tests pass, otherwise 0
            return (exitCode == 0) ? 100.0 : 0.0;
        } catch (Exception e) {
            System.err.println("Sandbox execution error: " + e.getMessage());
            return 0.0;
        }
    }
}