package com.crucible.crucible_backend.dto;

import java.util.List;

public record ProjectAnalysisDto(
        boolean is_clear,
        List<ClarifyingQuestionDto> clarifying_questions,
        List<ModuleBreakdownDto> modules
) {}