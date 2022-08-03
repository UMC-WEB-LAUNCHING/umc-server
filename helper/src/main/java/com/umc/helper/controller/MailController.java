package com.umc.helper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final JavaMailSender emailSender;

    @GetMapping("/api/mail")
    public void sentTestMail(){
        final MimeMessagePreparator preparator = (MimeMessage message) -> {

            final MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo("hojune0904@gmail.com");
            helper.setSubject("helper.com");
            helper.setText("테스트 실행중");
        };

        emailSender.send(preparator);

    }
}
