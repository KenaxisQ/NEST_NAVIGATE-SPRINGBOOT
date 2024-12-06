package com.kenaxisq.nestnavigate.registerMail.service;

import com.kenaxisq.nestnavigate.registerMail.entity.VerifyUserMail;

public interface VerifyUserMailService {
    String verifyUserMail(String email);
    public String isVerificationCodeValid(String email,String verificationCode);
    public VerifyUserMail getVerifyUserMailByEmail(String email);
}
