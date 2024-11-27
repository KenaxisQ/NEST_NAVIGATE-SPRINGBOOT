package com.kenaxisq.nestnavigate.user.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import com.kenaxisq.nestnavigate.utils.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmailOrPhone(String identifier) {
        Optional<User> user;
        if (identifier.contains("@")) {
            user = userRepository.findByEmail(identifier);
        } else {
            user = userRepository.findByPhone(identifier);
        }
        logger.info("User found: {}", user.isPresent());
        return user.orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user;
        user = userRepository.findByEmail(email);
        logger.info("User found: {}", user.toString());
        return user.orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public boolean validatePassword(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) { // Assuming the User entity has a getPassword() method
            return true;
        } else {
            logger.error("Invalid credentials");
            throw new ApiException(ErrorCodes.INVALID_CREDENTIALS);
        }
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            // Fetch user by identifier (email or phone)
            User user = userRepository.findById(resetPasswordDto.getUserId())
                    .orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND.getCode(), "User not found", HttpStatus.NOT_FOUND));

            // Verify the old password
            if (!passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Old password is incorrect", ErrorCodes.INVALID_OLD_PASSWORD.getCode()));
            }

            // Encode and set the new password
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            userRepository.save(user);

            return ResponseEntity.ok(ResponseBuilder.success(null, "Password reset successful"));
        } catch (ApiException e) {
            logger.error("Error resetting password: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus())
                    .body(ResponseBuilder.error(e));
        } catch (Exception e) {
            logger.error("Unexpected error during password reset: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", ErrorCodes.INTERNAL_SERVER_ERROR.getCode()));
        }
    }
}
