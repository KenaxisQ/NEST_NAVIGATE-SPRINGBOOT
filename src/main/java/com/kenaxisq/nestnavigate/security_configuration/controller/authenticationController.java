package com.kenaxisq.nestnavigate.security_configuration.controller;

import com.kenaxisq.nestnavigate.security_configuration.dto.*;
import com.kenaxisq.nestnavigate.security_configuration.service.AuthenticationService;

import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
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
        return authenticationService.loginWithOtp(loginWithOTPDto.getIdentifier());
    }
    @PostMapping("/validateEmailOtpLogin")
    public ResponseEntity<?> validateEmailOtpLogin(@RequestBody VerifyUserDto verifyUserDto) {
        return authenticationService.validateEmailOtpLogin(verifyUserDto.getEmail(), verifyUserDto.getVerificationCode());
    }
    @PostMapping("/validateGoogleAuthLogin")
    public ResponseEntity<?> LoginWithGoogleAuth(@RequestBody TokenDto tokenDto) {
        return authenticationService.validateGoogleAuthLogin(tokenDto);
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return authenticationService.forgotPassword(forgotPasswordDto);
    }
    @PutMapping("/verifyAndResetPassword")
    public ResponseEntity<?> verifyAndResetPassword(@RequestBody VerifyForgotPasswordDto verifyForgotPasswordDto) {
        return authenticationService.verifyAndResetPassword(verifyForgotPasswordDto);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDto tokenDto) {
        return authenticationService.refreshAccessToken(tokenDto);
    }


}
