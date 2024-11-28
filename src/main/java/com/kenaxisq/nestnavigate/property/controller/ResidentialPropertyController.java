package com.kenaxisq.nestnavigate.property.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/property")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
public class ResidentialPropertyController {
}
