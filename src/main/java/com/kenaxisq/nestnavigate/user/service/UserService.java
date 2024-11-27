package com.kenaxisq.nestnavigate.user.service;


import com.kenaxisq.nestnavigate.user.entity.User;

public interface UserService {

    public User findByEmailOrPhone(String identifier);
    public User findByEmail(String email);
    public boolean validatePassword(User user, String password);

}