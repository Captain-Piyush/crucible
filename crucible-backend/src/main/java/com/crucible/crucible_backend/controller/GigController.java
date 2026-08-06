package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.dto.GigCreateRequest;
import com.crucible.crucible_backend.dto.GigResponse;
import com.crucible.crucible_backend.service.GigService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gigs")
public class GigController {

    private final GigService gigService;

    public GigController(GigService gigService) {
        this.gigService = gigService;
    }

    @PostMapping
    public ResponseEntity<GigResponse> createGig(@Valid @RequestBody GigCreateRequest request) {
        GigResponse createdGig = gigService.createGig(request);
        return new ResponseEntity<>(createdGig, HttpStatus.CREATED);
    }
}