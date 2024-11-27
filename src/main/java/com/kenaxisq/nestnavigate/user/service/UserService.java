package com.kenaxisq.nestnavigate.user.service;

import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
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

    public User findByEmail(String email) {
        Optional<User> user;
        user = userRepository.findByEmail(email);
        logger.info("User found: {}", user.toString());
        return user.orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
    }

    public boolean validatePassword(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) { // Assuming the User entity has a getPassword() method
            return true;
        } else {
            logger.error("Invalid credentials");
            throw new ApiException(ErrorCodes.INVALID_CREDENTIALS);
        }
    }

}