package com.kenaxisq.nestnavigate.security_configuration.controller;

import com.kenaxisq.nestnavigate.registerMail.entity.VerifyUserMail;
import com.kenaxisq.nestnavigate.registerMail.service.VerifyUserMailService;
import com.kenaxisq.nestnavigate.security_configuration.dto.*;
import com.kenaxisq.nestnavigate.security_configuration.service.AuthenticationService;

import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class authenticationController {
    private final AuthenticationService authenticationService;
    private final VerifyUserMailService verifyUserMailService;
    @Autowired
    public authenticationController(AuthenticationService authenticationService,
                                    VerifyUserMailService verifyUserMailService) {
        this.authenticationService = authenticationService;
        this.verifyUserMailService = verifyUserMailService;
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
    public ResponseEntity<ApiResponse<String>> verifyUser(@RequestBody LoginWithOTPDto loginWithOTPDto) {
        return ResponseEntity.ok(ResponseBuilder.success(verifyUserMailService.verifyUserMail(loginWithOTPDto.getIdentifier()), "User Verified Successfully"));
    }
    @PostMapping("/validateUserEmail")
    public ResponseEntity<ApiResponse<String>> validateUserEmail(@RequestBody VerifyUserDto verifyUserDto) {
        return ResponseEntity.ok(ResponseBuilder.success(verifyUserMailService.isVerificationCodeValid(verifyUserDto.getIdentifier(),verifyUserDto.getVerificationCode()), "User Verified Successfully"));
    }

    @PostMapping("/loginWithOTP")
    public ResponseEntity<?> loginWithOTP(@RequestBody LoginWithOTPDto loginWithOTPDto) {
        return authenticationService.loginWithOtp(loginWithOTPDto.getIdentifier());
    }
    @PostMapping("/validateEmailOtpLogin")
    public ResponseEntity<?> validateEmailOtpLogin(@RequestBody VerifyUserDto verifyUserDto) {
        return authenticationService.validateEmailOtpLogin(verifyUserDto.getIdentifier(), verifyUserDto.getVerificationCode());
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
