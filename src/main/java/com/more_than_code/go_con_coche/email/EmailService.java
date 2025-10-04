package com.more_than_code.go_con_coche.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class    EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendRegistrationEmail(String to, String username) {
        Context context = new Context();
        context.setVariable("username", username);
        String html = templateEngine.process("welcome-email", context);
        sendHtmlEmail(to, "Registration to GoConCoche was successful", html);
    }

    public void sendResetPasswordEmail(String to, String username) {
        Context context = new Context();
        context.setVariable("username", username);
        String html = templateEngine.process("forgot-password-email", context);
        sendHtmlEmail(to, "Password reset request to your GoConCoche account", html);
    }

    public void sendConfirmationEmail(String to, String username, String reservationCode, LocalDateTime startDate, LocalDateTime endDate, String vehicleBrand, int vehicleYear, String vehicleColor, String seater, int childSeatsNumber, String vehicleModel, int countTime, int priceHour, int totalPrice, String location, String imageUrl) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("reservationCode", reservationCode);
        context.setVariable("startDate", startDate);
        context.setVariable("endDate", endDate);
        context.setVariable("vehicleBrand", vehicleBrand);
        context.setVariable("vehicleYear", vehicleYear);
        context.setVariable("vehicleColor", vehicleColor);
        context.setVariable("seater", seater);
        context.setVariable("childSeatsNumber", childSeatsNumber);
        context.setVariable("vehicleModel", vehicleModel);
        context.setVariable("countTime", countTime);
        context.setVariable("priceHour", priceHour);
        context.setVariable("totalPrice", totalPrice);
        context.setVariable("location", location);
        context.setVariable("imageUrl", imageUrl);

        String html = templateEngine.process("reservation-email", context);
        sendHtmlEmail(to, "Reservation on GoConCoche was confirmed", html);
    }

    public void sendReminderEmail(String to, String username, String reservationCode,
                                  LocalDateTime startDate, LocalDateTime endDate,
                                  String vehicleBrand, int vehicleYear, String vehicleColor,
                                  String seater, int childSeatsNumber, String vehicleModel,
                                  int countTime, int priceHour, int totalPrice,
                                  String location, String imageUrl) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("reservationCode", reservationCode);
        context.setVariable("startDate", startDate);
        context.setVariable("endDate", endDate);
        context.setVariable("vehicleBrand", vehicleBrand);
        context.setVariable("vehicleYear", vehicleYear);
        context.setVariable("vehicleColor", vehicleColor);
        context.setVariable("seater", seater);
        context.setVariable("childSeatsNumber", childSeatsNumber);
        context.setVariable("vehicleModel", vehicleModel);
        context.setVariable("countTime", countTime);
        context.setVariable("priceHour", priceHour);
        context.setVariable("totalPrice", totalPrice);
        context.setVariable("location", location);
        context.setVariable("imageUrl", imageUrl);

        String html = templateEngine.process("reminder-email", context);
        sendHtmlEmail(to, "Reminder: Your GoConCoche reservation starts in 1 hour!", html);
    }

    private void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom("goconcoche@gmail.com", "GoConCoche App");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}