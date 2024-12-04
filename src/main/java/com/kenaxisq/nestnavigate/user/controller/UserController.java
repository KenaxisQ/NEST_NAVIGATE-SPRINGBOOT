package com.kenaxisq.nestnavigate.user.controller;

import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.service.UserService;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/emailOrPhone/{identifier}")
    public ResponseEntity<User> getUserByEmailOrPhone(@PathVariable String identifier) {
        return ResponseEntity.ok(userService.findByEmailOrPhone(identifier));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return  ResponseEntity.ok(ResponseBuilder.success(userService.resetPassword(resetPasswordDto)));
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(ResponseBuilder.success(userService.updateUser(user),"User Update Successful").getData());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(ResponseBuilder.success(userService.deleteUser(id)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(ResponseBuilder.success(userService.getUser(id),"User Retrieved Successfully").getData());
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(ResponseBuilder.success(userService.getUsers(),"Users Retrieved Successfully").getData());
    }
    @PutMapping("/addToFavourites/{userId}")
    public ResponseEntity<ApiResponse<String>> addToFavourites(@PathVariable String userId, @RequestBody String wishList) {
        return ResponseEntity.ok(ResponseBuilder.success(userService.updateFavourites(userId, wishList)));
    }

}
//class-level requirement
//@Operation(summary = "Public Endpoint", description = "Does not require authentication", security = {})