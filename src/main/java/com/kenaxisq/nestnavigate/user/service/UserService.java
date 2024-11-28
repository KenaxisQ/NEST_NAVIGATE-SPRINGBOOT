package com.kenaxisq.nestnavigate.user.service;


import com.kenaxisq.nestnavigate.user.dto.ResetPasswordDto;
import com.kenaxisq.nestnavigate.user.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    public User findByEmailOrPhone(String identifier);
    public User findByEmail(String email);
    public boolean validatePassword(User user, String password);
    public String resetPassword(ResetPasswordDto resetPasswordDto);
    public User updateUser(User user);
    public String deleteUser(String id);
    public User getUser(String id);
    public List<User> getUsers();
    public void updatePropertyListingLimit(String userId, Integer limit);
}