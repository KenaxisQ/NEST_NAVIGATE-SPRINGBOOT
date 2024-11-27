package com.kenaxisq.nestnavigate.user.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

}
