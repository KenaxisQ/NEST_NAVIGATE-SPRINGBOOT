package com.kenaxisq.nestnavigate.user.dto;

import lombok.Getter;

@Getter
public class ResetPasswordDto {
    String userId;
    String oldPassword;
    String newPassword;
}
