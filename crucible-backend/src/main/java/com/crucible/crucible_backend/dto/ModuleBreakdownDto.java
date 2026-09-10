package com.crucible.crucible_backend.dto;

public record ModuleBreakdownDto(
        String module_name,
        String description,
        int priority
) {}