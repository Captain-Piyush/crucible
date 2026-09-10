package com.crucible.crucible_backend.dto;

import java.util.List;

public record ClarifyingQuestionDto(
        String question,
        List<String> options
) {}