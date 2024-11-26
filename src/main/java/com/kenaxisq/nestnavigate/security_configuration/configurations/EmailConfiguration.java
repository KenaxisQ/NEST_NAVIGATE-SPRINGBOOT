package com.kenaxisq.nestnavigate.security_configuration.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
      JavaMailSenderImpl sendMail = new JavaMailSenderImpl();
      sendMail.setHost("smtp.gmail.com");
      sendMail.setPort(587);
      sendMail.setUsername(email);
      sendMail.setPassword(password);
      Properties props = sendMail.getJavaMailProperties();
      props.put("mail.transport.protocol", "smtp");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.debug", "true");
      return sendMail;

    }
}
