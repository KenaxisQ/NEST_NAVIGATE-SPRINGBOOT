package com.kenaxisq.nestnavigate.security_configuration.controller;

import com.kenaxisq.nestnavigate.security_configuration.dto.LoginUserDto;
import com.kenaxisq.nestnavigate.security_configuration.dto.LoginWithOTPDto;
import com.kenaxisq.nestnavigate.security_configuration.dto.RegisterUserDto;
import com.kenaxisq.nestnavigate.security_configuration.dto.VerifyUserDto;
import com.kenaxisq.nestnavigate.security_configuration.service.AuthenticationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authenticationController {
    private final AuthenticationService authenticationService;
    public authenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUserDto) {
        return authenticationService.login(loginUserDto.getIdentifier(), loginUserDto.getPassword());
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto user) {
        return authenticationService.register(user);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto user) {
        return authenticationService.verifyUser(user.getEmail(),user.getVerificationCode());
    }
    @PostMapping("/loginWithOTP")
    public ResponseEntity<?> loginWithOTP(@RequestBody LoginWithOTPDto loginWithOTPDto) {
        return authenticationService.loginWithOtp(loginWithOTPDto.getEmail());
    }
    @PostMapping("/validateEmailOtpLogin")
    public ResponseEntity<?> validateEmailOtpLogin(@RequestBody VerifyUserDto verifyUserDto) {
        return authenticationService.validateEmailOtpLogin(verifyUserDto.getEmail(), verifyUserDto.getVerificationCode());
    }
    @PostMapping("/validateGoogleAuthLogin")
    public ResponseEntity<?> LoginWithGoogleAuth(@RequestBody String token) {
        return authenticationService.validateGoogleAuthLogin(token);
    }


}
