package com.kenaxisq.nestnavigate.user.controller;

import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<User> getUser(@PathVariable String identifier) {
        return ResponseEntity.ok(userService.findByEmailOrPhone(identifier));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userService.resetPassword(resetPasswordDto);
    }
}
//class-level requirement
//@Operation(summary = "Public Endpoint", description = "Does not require authentication", security = {})