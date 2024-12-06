package com.kenaxisq.nestnavigate.user.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.custom_exceptions.ErrorCodes;
import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
import com.kenaxisq.nestnavigate.user.entity.User;
import com.kenaxisq.nestnavigate.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
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
            user = userRepository.findByEmail(identifier.toLowerCase().trim());
        } else {
            user = userRepository.findByPhone(identifier.trim());
        }
        logger.info("User found: {}", user.isPresent());
        return user.orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user;
        user = userRepository.findByEmail(email.toLowerCase().trim());
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
    public String resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            // Fetch user by identifier (email or phone)
            User user = userRepository.findById(resetPasswordDto.getUserId())
                    .orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND.getCode(), "User not found", HttpStatus.NOT_FOUND));

            // Verify the old password
            if (!passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
                throw new ApiException(ErrorCodes.INVALID_OLD_PASSWORD);
                }

            // Encode and set the new password
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            userRepository.save(user);

            return "Password reset successful";
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during password reset: {}", e.getMessage());
            throw new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(), "An unexpected error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            // Fetch existing user by id
            Optional<User> optionalExistingUser = userRepository.findById(user.getId());
            if (optionalExistingUser.isEmpty()) {
                throw new ApiException(ErrorCodes.USER_NOT_FOUND);
            }
            User existingUser = optionalExistingUser.get();

            if (!StringUtils.hasText(user.getEmail())) {
                user.setEmail(existingUser.getEmail().toLowerCase().trim());
            }

            // Validate phone number for uniqueness
            if (user.getPhone() != null) {
                Optional<User> optionalUserByPhone = userRepository.findByPhone(user.getPhone());
                if (optionalUserByPhone.isPresent() && !optionalUserByPhone.get().getId().equals(user.getId())) {
                    throw new ApiException(ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS.getCode(),
                            ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS.getMessage(),
                            ErrorCodes.PHONE_NUMBER_ALREADY_EXISTS.getHttpStatus());
                }
                existingUser.setPhone(user.getPhone());
            }

            // Update non-null fields using helper method
            updateNonNullFields(user, existingUser);

            // Save and return the updated user
            return userRepository.save(existingUser);
        } catch (ApiException e) {
            logger.error("Error updating user: {}", e.getMessage());
            throw e; // Rethrow to be handled by global exception handler
        } catch (Exception e) {
            logger.error("Unexpected error during user update: {}", e.getMessage());
            throw new ApiException(ErrorCodes.INTERNAL_SERVER_ERROR.getCode(),
                    "An unexpected error occurred while updating the user",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteUser(String id) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
            userRepository.deleteById(id);
            return "User deleted successfully";

        } catch (ApiException ex) {
            logger.error("API error while deleting user with ID {}: {}", id, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting user with ID {}: {}", id, ex.getMessage());
            throw new ApiException("UNKNOWN_EXCEPTION", ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public User getUser(String id) {
        try {
            logger.info("User ServiceImpl: getUser() : Fetching User From DB for user id: {}", id);
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                throw new ApiException(ErrorCodes.USER_NOT_FOUND);
            }
            User user = userOptional.get();
            logger.info("User ServiceImpl: getUser() : Fetch successful for user id: {}", id);
            return user;
        } catch (ApiException e) {
            logger.error("Error while retrieving user id: {}", id, e);
            throw e; // Re-throwing ApiException to ensure correct error codes are propagated
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving user id: {}", id, e);
            throw new ApiException("GETUSER_ERR", "An unexpected error occurred while retrieving the user", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<User> getUsers() {
        try {
            logger.info("User Controller: getUsers() : Fetching all the Users from DB....");
            Iterable<User> usersList = userRepository.findAll();
            if (!usersList.iterator().hasNext()) {
                logger.warn("User Controller: getUsers() : No users found in the DB.");
                throw new ApiException(ErrorCodes.USER_EMPTY);
            }
            List<User> users = new ArrayList<>();
            usersList.forEach(users::add);
            return users;

        } catch (Exception e) {
            logger.error(String.format("User Controller: getUsers() : Error occurred"), e);
            throw new ApiException(e.getMessage(),"GETUSERS_ERR",HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void updatePropertyListingLimit(String userId, Integer limit){
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
        user.setProperties_listing_limit(limit);
        userRepository.save(user);
        logger.info("Property Listing Limit for User {} updated to: {}",user.getName(),user.getProperties_listing_limit());
//            return String.format("Property Listing Limit for User %s updated to: %d",user.getName(),user.getProperties_listing_limit());
    }

    @Override
    public String updateProfilePicture(String userId, String profilePicture) {
        User user = getUser(userId);
        user.setProfilePic(profilePicture);
        userRepository.save(user);
        logger.info("UserServiceImpl: updateProfilePicture(): Profile Picture for User {} updated successfully :) ",user.getName());
        return String.format("Profile Picture for User %s updated successfully :) ",user.getName());
    }

    @Override
    public void updatePropertyListed(String userId, Integer limit){
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCodes.USER_NOT_FOUND));
        user.setProperties_listed(limit);
        userRepository.save(user);
        logger.info("Property Listed for User {} updated to: {}",user.getName(),user.getProperties_listed());
//            return String.format("Property Listing Limit for User %s updated to: %d",user.getName(),user.getProperties_listing_limit());
    }

    @Override
    public String updateFavourites(String userId, String properties) {
        User user = getUser(userId);
        user.setFavourites(properties);
        userRepository.save(user);
        return String.format("Favourites %s updated successfully for user %s", properties, user.getName());
    }

    @Override
    public List<User> getUsersByIds(List<String> ids) {
        try{
            List<User> users = userRepository.findUsersByIds(ids);
            if(users.isEmpty()){
                throw new ApiException("USERS_EMPTY", "No Users found", HttpStatus.NOT_FOUND);
            }
            return users;
        }
        catch (ApiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApiException("ERR_USERS_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private void updateNonNullFields(User source, User target) {
        if (StringUtils.hasText(source.getName())) target.setName(source.getName());
        if (StringUtils.hasText(source.getPassword())) target.setPassword(passwordEncoder.encode(source.getPassword()));
        if (source.getRole() != null) target.setRole(source.getRole());
        if (source.getProperties_listed() > 0) target.setProperties_listed(source.getProperties_listed());
        if (source.getProperties_listing_limit() > 0) target.setProperties_listing_limit(source.getProperties_listing_limit());
        target.setActive(source.isActive());
    }
}
