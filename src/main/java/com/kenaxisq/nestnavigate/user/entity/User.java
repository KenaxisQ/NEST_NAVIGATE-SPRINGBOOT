package com.kenaxisq.nestnavigate.user.entity;

import com.kenaxisq.nestnavigate.utils.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;

    @Column(nullable = false)
    private String Name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private int properties_listed;

    @Column(nullable = false)
    private int properties_listing_limit;

    @Column(name="verification_code")
    private String verificationCode;

    @Column(name="verification_expires_at")
    private LocalDateTime verificationCodeExpiresAt;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean userVerified = false;

    @Column(nullable = true)
    private String profilePic;

    @Column(nullable = true)
    private String favourites;

    public User(String name, String email, String phone, String password) {
        setName(name);
        setEmail(email);
        setPhone(phone);
        setPassword(password);
    }

    @PrePersist
    protected void onCreate() {
        initializeDefaults();
    }

    private void initializeDefaults() {
        this.createdAt = LocalDateTime.now();
        this.properties_listing_limit = 5;
        this.properties_listed = 0;
        this.role = UserRole.USER;
        this.isActive = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userVerified;
    }

    public void incrementPropertiesListed() {
        setProperties_listed(getProperties_listed()+1);
    }
}


