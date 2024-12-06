package com.kenaxisq.nestnavigate.registerMail.service;

import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import com.kenaxisq.nestnavigate.registerMail.repository.VerifyUserMailRepository;
import com.kenaxisq.nestnavigate.registerMail.entity.VerifyUserMail;
import com.kenaxisq.nestnavigate.security_configuration.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
public class VerifyMailServiceImpl implements VerifyUserMailService {

    private final VerifyUserMailRepository verifyUserMailRepository;
    private final EmailService emailService;

    @Autowired
    public VerifyMailServiceImpl(VerifyUserMailRepository verifyUserMailRepository, EmailService emailService) {
        this.verifyUserMailRepository = verifyUserMailRepository;
        this.emailService = emailService;
    }

    @Override
    public String verifyUserMail(String email) {
        try {
            sendVerificationCodeToEmail(email);
            return "Verification code sent to " + email;
        } catch (MessagingException e) {
            throw new ApiException(e.getMessage(),
                    "Failed to send verification email to " + email,HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

    @Override
    public String isVerificationCodeValid(String email, String verificationCode) {
        Optional<VerifyUserMail> optionalVerifyUserMail = verifyUserMailRepository.findById(email);
        if (optionalVerifyUserMail.isEmpty()) {
            throw new ApiException("INVALID_MAIL", "Invalid Mail ID", HttpStatus.BAD_REQUEST);
        }
        VerifyUserMail verifyUserMail = optionalVerifyUserMail.get();
        if(verifyUserMail.getVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new ApiException("TOKEN_EXPIRED", "The verification code has expired", HttpStatus.BAD_REQUEST);
        }
        if (verifyUserMail.getVerificationCode().equals(verificationCode)) {
            verifyUserMail.setVerified(true);
            verifyUserMailRepository.save(verifyUserMail);
            return "Verification Code is Valid";
        } else {
            throw new ApiException("INVALID_VERIFICATION_CODE", "Invalid Verification Code", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public VerifyUserMail getVerifyUserMailByEmail(String email) {
        Optional<VerifyUserMail> optionalVerifyUserMail = verifyUserMailRepository.findById(email);
        if (optionalVerifyUserMail.isEmpty()) {
            throw new ApiException("INVALID_MAIL", "Invalid Mail ID", HttpStatus.BAD_REQUEST);
        }
        return optionalVerifyUserMail.get();
    }

    private void sendVerificationCodeToEmail(String email) throws MessagingException {
        String subject = "Account Verification";
        String verificationCode = generateVerificationCode();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        verifyUserMailRepository.save(new VerifyUserMail(email, verificationCode, expiryTime,false));

        String htmlMessage = buildHtmlMessage(subject, verificationCode, expiryTime);
        emailService.sendVerificationEmail(email, subject, htmlMessage);
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    private String buildHtmlMessage(String subject, String verificationCode, LocalDateTime expiryTime) {
        String introMessage = "Welcome to Nest Navigate! To ensure the security of your account, please enter the verification code below to complete your registration:";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy hh:mm a", Locale.ENGLISH);
        String formattedExpiryTime = expiryTime.format(formatter);

        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + subject + "</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #eaeaea; margin: 0; padding: 0; }" +
                ".container { background-color: #ffffff; margin: 20px auto; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); max-width: 600px; }" +
                "h2 { color: #333; text-align: center; }" +
                "p { font-size: 16px; line-height: 1.5; color: #555; }" +
                ".verification-code { font-size: 24px; font-weight: bold; color: #007bff; text-align: center; padding: 10px; border: 2px solid #007bff; border-radius: 5px; display: inline-block; margin: 20px 0; cursor: pointer; }" +
                ".footer { text-align: center; font-size: 12px; color: #aaa; margin-top: 20px; }" +
                "</style>" +
                "<script>" +
                "function copyToClipboard() { const code = document.getElementById('verificationCode').innerText; navigator.clipboard.writeText(code).then(() => { alert('Verification code copied to clipboard!'); }).catch(err => { console.error('Failed to copy: ', err); }); }" +
                "</script>" +
                "</head>" +
                "<body>" +
                "<div class=\"container\">" +
                "<h2>" + subject + "</h2>" +
                "<p>" + introMessage + "</p>" +
                "<div style=\"text-align:center\">" +
                "<div id=\"verificationCode\" class=\"verification-code\" onclick=\"copyToClipboard()\">" + verificationCode + "</div>" +
                "<span style=\"display: block; text-align: center; font-size: 12px; color: #777;\">Valid until " + formattedExpiryTime + "</span>" +
                "</div>" +
                "<p>If you did not request this, please disregard this email.</p>" +
                "<div class=\"footer\">" +
                "<p>&copy; 2028 Nest Navigate. All rights reserved.</p>" +
                "<p>Srikakulam, Andhrapradesh, India</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}