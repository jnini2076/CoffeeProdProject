package com.coffeewa.coffeewebapp.jobapplications;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeewa.coffeewebapp.service.SESservice;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Application")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final SESservice sesService;

    public ApplicationController(ApplicationService applicationService, SESservice sesService) {
        this.applicationService = applicationService;
        this.sesService = sesService;
    }

    @PostMapping
    public ResponseEntity<Void> submitApplication(@RequestBody @Valid ApplicationDTO dto) {
        applicationService.saveApplication(dto);
        sesService.GenerateEmail(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
