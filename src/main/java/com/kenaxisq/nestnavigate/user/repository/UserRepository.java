package com.kenaxisq.nestnavigate.user.repository;

import com.kenaxisq.nestnavigate.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String>{

//    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.phone = :identifier")
    Optional<User> findByEmailOrPhone(String identifier);
    Optional<User> findByVerificationCode(String verificationCode);
    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findUsersByIds(List<String> ids);

    List<String> id(String id);
}
