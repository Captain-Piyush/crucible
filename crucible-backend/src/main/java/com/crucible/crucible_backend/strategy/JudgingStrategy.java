package com.crucible.crucible_backend.strategy;

public interface JudgingStrategy {
    boolean supports(String gigType);
    Double evaluate(String deliverableReference);
}