package com.kenaxisq.nestnavigate.security_configuration.service;

import com.kenaxisq.nestnavigate.security_configuration.dto.AuthenticationResponse;
import com.kenaxisq.nestnavigate.security_configuration.dto.RegisterUserDto;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import com.kenaxisq.nestnavigate.user.service.UserService;

import com.kenaxisq.nestnavigate.utils.ApiResponse;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 UserService userService,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.emailService = emailService;
    }

    public ResponseEntity<?> login(String identifier, String password) {
        try {
            User user = userService.findByEmailOrPhone(identifier);
            if (userService.validatePassword(user, password)) {
                if(!user.isUserVerified())
                    throw new ApiException(ErrorCodes.USER_NOT_VERIFIED);
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                jwtService.saveToken(accessToken, user, false);
                jwtService.saveToken(refreshToken, user, true);
                AuthenticationResponse response = new AuthenticationResponse(accessToken, refreshToken, "Login successful");
                return ResponseEntity.ok(ResponseBuilder.success(response));
            }
        } catch (ApiException e) {
            logger.error("Login error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(ResponseBuilder.error(e));
        } catch (Exception e) {
            logger.error("Unexpected error during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseBuilder.error(HttpStatus.UNAUTHORIZED, "Invalid credentials", ErrorCodes.INVALID_CREDENTIALS.getCode()));
    }

    public ResponseEntity<?> loginWithOtp(String email) {
        try {
            User user = userService.findByEmail(email);
            if (!user.isUserVerified())
                throw new ApiException(ErrorCodes.USER_NOT_VERIFIED);
           return ResponseEntity.ok(ResponseBuilder.success(user, "OTP sent successful!!"));
        } catch (ApiException e) {
            logger.error("Error: while sending OTP to User : {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(ResponseBuilder.error(e));
        } catch (Exception e) {
            logger.error("Unexpected Error: while sending OTP to User: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    public ResponseEntity<ApiResponse<?>> validateEmailOtpLogin(String email, String otp) {
        try {
            User user = userService.findByEmail(email);
            if (!user.isUserVerified()) {
                throw new ApiException(ErrorCodes.USER_NOT_VERIFIED);
            }
            if (user.getVerificationCode().equals(otp)) {
                String accessToken = jwtService.generateAccessToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                return ResponseEntity.ok(ResponseBuilder.success(new AuthenticationResponse(accessToken, refreshToken, "Login Successful")));
            } else {
                ApiResponse<AuthenticationResponse> errorResponse = ApiResponse.<AuthenticationResponse>builder()
                        .message("Invalid OTP")
                        .build();
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (ApiException e) {
            logger.error("Error while validating OTP for User: {}", e.getMessage());
            ApiResponse<AuthenticationResponse> errorResponse = ApiResponse.<AuthenticationResponse>builder()
                    .message("Error while validating OTP")
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            logger.error("Unexpected Error: while validating OTP for User: {}", e.getMessage());
            ApiResponse<AuthenticationResponse> errorResponse = ApiResponse.<AuthenticationResponse>builder()
                    .message("Unexpected Error")
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    public ResponseEntity<?> validateGoogleAuthLogin(String token) {
        try{
            String email = jwtService.decodeGoogleJwtToken(token);
            if (email == null) {
                throw new ApiException(ErrorCodes.INVALID_TOKEN);
            }
            User user = userService.findByEmail(email);
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return ResponseEntity.ok(ResponseBuilder.success(new AuthenticationResponse(accessToken, refreshToken, "Login Successful")));
        }
        catch (ApiException e) {
            logger.error("Error while validating Google Auth Login: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(ResponseBuilder.error(e));
        }
        catch (Exception e) {
            logger.error("Unexpected Error: while validating Google Auth Login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    public ResponseEntity<?> register(RegisterUserDto user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByPhone(user.getPhone()).isPresent()) {
                throw new ApiException(ErrorCodes.USER_ALREADY_EXISTS);
            }
            User createuser = new User(user.getName(), user.getEmail(),user.getPhone(),user.getPassword());
            createuser.setPassword(passwordEncoder.encode(user.getPassword()));

            User savedUser = userRepository.save(createuser);

            // Send the verification email
            sendVerificationCodeToEmail(savedUser);

            return ResponseEntity.ok(ResponseBuilder.success(savedUser, "Registration successful"));
        } catch (ApiException e) {
            logger.error("Registration error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(ResponseBuilder.error(e));
        } catch (Exception e) {
            logger.error("Unexpected error during registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    public ResponseEntity<?> refreshAccessToken(String refreshToken) {
        try {
            User user = userRepository.findByEmail(jwtService.extractUserId(refreshToken))
                    .orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
            if (jwtService.isValidRefreshToken(refreshToken, user)) {
                String newAccessToken = jwtService.generateAccessToken(user);
                jwtService.saveToken(newAccessToken, user, false);
                AuthenticationResponse response = new AuthenticationResponse(newAccessToken, refreshToken, "Token refreshed successfully");
                return ResponseEntity.ok(ResponseBuilder.success(response));
            } else {
                throw new ApiException(ErrorCodes.INVALID_TOKEN);
            }
        } catch (ApiException e) {
            logger.error("Token refresh error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(ResponseBuilder.error(e));
        } catch (Exception e) {
            logger.error("Unexpected error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    public ResponseEntity<?> sendVerificationEmail(User user) {
        try {
            // Generate and set the verification code
            String verificationCode = generateVerificationCode();
            user.setVerificationCode(verificationCode);
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1)); // Code expires in 1 hour
            userRepository.save(user);

            // Send the email with the verification code
            sendVerificationCodeToEmail(user);

            return ResponseEntity.ok(ResponseBuilder.success(null, "Verification email sent successfully"));
        } catch (MessagingException e) {
            logger.error("Error sending verification email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send verification email", ErrorCodes.EXTERNAL_API_ERROR.getCode()));
        } catch (Exception e) {
            logger.error("Unexpected error during send verification email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    public ResponseEntity<?> verifyUser(String email, String verificationCode) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
            if (verificationCode.equals(user.getVerificationCode()) && user.getVerificationCodeExpiresAt().isAfter(LocalDateTime.now())) {
                user.setUserVerified(true);
                userRepository.save(user);
                return ResponseEntity.ok(ResponseBuilder.success(null, "User verified successfully"));
            } else {
                throw new ApiException(ErrorCodes.INVALID_CREDENTIALS);
            }
        } catch (ApiException e) {
            logger.error("User verification error: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(ResponseBuilder.error(e));
        } catch (Exception e) {
            logger.error("Unexpected error during user verification: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }

    private void sendVerificationCodeToEmail(User user) throws MessagingException {
        String subject = "Account Verification";
        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        user.setVerificationCodeExpiresAt(expiryTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy hh:mm a", Locale.ENGLISH);
        String formattedExpiryTime = expiryTime.format(formatter) ;
        userRepository.save(user);

        String htmlMessage = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "    <meta charset=\"UTF-8\">"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "    <title>Verification Email</title>"
                + "    <style>"
                + "        body {"
                + "            font-family: Arial, sans-serif;"
                + "            background-color: #eaeaea;"
                + "            margin: 0;"
                + "            padding: 0;"
                + "        }"
                + "        .container {"
                + "            background-color: #ffffff;"
                + "            margin: 20px auto;"
                + "            padding: 30px;"
                + "            border-radius: 8px;"
                + "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);"
                + "            max-width: 600px;"
                + "        }"
                + "        h2 {"
                + "            color: #333;"
                + "            text-align: center;"
                + "        }"
                + "        p {"
                + "            font-size: 16px;"
                + "            line-height: 1.5;"
                + "            color: #555;"
                + "        }"
                + "        .verification-code {"
                + "            font-size: 24px;"
                + "            font-weight: bold;"
                + "            color: #007bff;"
                + "            text-align: center;"
                + "            padding: 10px;"
                + "            border: 2px solid #007bff;"
                + "            border-radius: 5px;"
                + "            display: inline-block;"
                + "            margin: 20px 0;"
                + "            cursor: pointer;"
                + "        }"
                + "        .footer {"
                + "            text-align: center;"
                + "            font-size: 12px;"
                + "            color: #aaa;"
                + "            margin-top: 20px;"
                + "        }"
                + "        .image-container {"
                + "            text-align: center;"
                + "            margin: 20px 0;"
                + "        }"
                + "        .image-container img {"
                + "            width: 100%;"
                + "            height: auto;"
                + "            border-radius: 8px;"
                + "        }"
                + "        .button {"
                + "            display: inline-block;"
                + "            background-color: #007bff;"
                + "            color: #fff;"
                + "            padding: 10px 20px;"
                + "            text-decoration: none;"
                + "            border-radius: 5px;"
                + "            margin: 20px auto;"
                + "            text-align: center;"
                + "        }"
                + "    </style>"
                + "    <script>"
                + "        function copyToClipboard() {"
                + "            const code = document.getElementById('verificationCode').innerText;"
                + "            navigator.clipboard.writeText(code).then(() => {"
                + "                alert('Verification code copied to clipboard!');"
                + "            }).catch(err => {"
                + "                console.error('Failed to copy: ', err);"
                + "            });"
                + "        }"
                + "    </script>"
                + "</head>"
                + "<body>"
                + "    <div class=\"container\">"
                + "        <h2>Welcome to Nest Navigate!</h2>"
                + "        <p>Thank you for choosing Nest Navigate for your real estate needs. To ensure the security of your account, please enter the verification code below:</p>"
                + "        <div style=\"text-align:center\">"
                + "            <div id=\"verificationCode\" class=\"verification-code\" onclick=\"copyToClipboard()\">" + verificationCode + "</div>"
                + "            <span style=\"display: block; text-align: center; font-size: 12px; color: #777;\">Valid until " + formattedExpiryTime + "</span>"
                + "        </div>"
                + "        <div class=\"image-container\">"
                + "            <img src=\"https://tinyurl.com/nestnavigate\" alt=\"Real Estate\">"
                + "        </div>"
                + "        <p>If you did not request this verification, please disregard this email.</p>"
                + "        <div class=\"footer\">"
                + "            <p>&copy; 2028 Nest Navigate. All rights reserved.</p>"
                + "            <p>Srikakulam, Andhrapradesh, India</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
    }

    private String getDayOfMonthSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}