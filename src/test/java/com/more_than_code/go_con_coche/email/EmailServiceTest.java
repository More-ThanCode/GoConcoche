package com.more_than_code.go_con_coche.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for EmailService")
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    private String recipientEmail;
    private String username;
    private String htmlContent;

    @BeforeEach
    void setUp() {
        recipientEmail = "test@example.com";
        username = "testUser";
        htmlContent = "<html><body>Test email content</body></html>";
    }

    @Nested
    @DisplayName("sendRegistrationEmail() tests")
    class SendRegistrationEmailTests {

        @Test
        @DisplayName("Should send registration email successfully with correct template and variables")
        void sendRegistrationEmail_WhenValidData_ShouldSendEmailSuccessfully() throws Exception {

            when(templateEngine.process(eq("welcome-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendRegistrationEmail(recipientEmail, username);

            ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
            verify(templateEngine).process(eq("welcome-email"), contextCaptor.capture());

            Context capturedContext = contextCaptor.getValue();
            assertEquals(username, capturedContext.getVariable("username"));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        @DisplayName("Should use correct template name for registration email")
        void sendRegistrationEmail_ShouldUseCorrectTemplate() {

            when(templateEngine.process(eq("welcome-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendRegistrationEmail(recipientEmail, username);

            verify(templateEngine).process(eq("welcome-email"), any(Context.class));
        }

        @Test
        @DisplayName("Should handle exception when sending registration email fails")
        void sendRegistrationEmail_WhenExceptionThrown_ShouldHandleGracefully() {

            when(templateEngine.process(eq("welcome-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Mail server error"));

            assertDoesNotThrow(() -> emailService.sendRegistrationEmail(recipientEmail, username));

            verify(mailSender).createMimeMessage();
            verify(mailSender, never()).send(any(MimeMessage.class));
        }
    }

    @Nested
    @DisplayName("sendConfirmationEmail() tests")
    class SendConfirmationEmailTests {

        private String reservationCode;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String vehicleBrand;
        private int vehicleYear;
        private String vehicleColor;
        private String seater;
        private int childSeatsNumber;
        private String vehicleModel;
        private int countTime;
        private int priceHour;
        private int totalPrice;
        private String location;
        private String imageUrl;

        @BeforeEach
        void setUp() {
            reservationCode = "RES123456";
            startDate = LocalDateTime.of(2025, 10, 15, 10, 0);
            endDate = LocalDateTime.of(2025, 10, 15, 18, 0);
            vehicleBrand = "Toyota";
            vehicleYear = 2023;
            vehicleColor = "Blue";
            seater = "5";
            childSeatsNumber = 2;
            vehicleModel = "Corolla";
            countTime = 8;
            priceHour = 15;
            totalPrice = 120;
            location = "Madrid";
            imageUrl = "http://example.com/car.jpg";
        }

        @Test
        @DisplayName("Should send confirmation email successfully with all reservation details")
        void sendConfirmationEmail_WhenValidData_ShouldSendEmailSuccessfully() throws Exception {

            when(templateEngine.process(eq("reservation-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendConfirmationEmail(recipientEmail, username, reservationCode, startDate, endDate, vehicleBrand, vehicleYear, vehicleColor, seater, childSeatsNumber, vehicleModel, countTime, priceHour, totalPrice, location, imageUrl);

            ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
            verify(templateEngine).process(eq("reservation-email"), contextCaptor.capture());

            Context capturedContext = contextCaptor.getValue();
            assertEquals(username, capturedContext.getVariable("username"));
            assertEquals(reservationCode, capturedContext.getVariable("reservationCode"));
            assertEquals(startDate, capturedContext.getVariable("startDate"));
            assertEquals(endDate, capturedContext.getVariable("endDate"));
            assertEquals(vehicleBrand, capturedContext.getVariable("vehicleBrand"));
            assertEquals(vehicleYear, capturedContext.getVariable("vehicleYear"));
            assertEquals(vehicleColor, capturedContext.getVariable("vehicleColor"));
            assertEquals(seater, capturedContext.getVariable("seater"));
            assertEquals(childSeatsNumber, capturedContext.getVariable("childSeatsNumber"));
            assertEquals(vehicleModel, capturedContext.getVariable("vehicleModel"));
            assertEquals(countTime, capturedContext.getVariable("countTime"));
            assertEquals(priceHour, capturedContext.getVariable("priceHour"));
            assertEquals(totalPrice, capturedContext.getVariable("totalPrice"));
            assertEquals(location, capturedContext.getVariable("location"));
            assertEquals(imageUrl, capturedContext.getVariable("imageUrl"));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        @DisplayName("Should use correct template name for confirmation email")
        void sendConfirmationEmail_ShouldUseCorrectTemplate() {

            when(templateEngine.process(eq("reservation-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendConfirmationEmail(recipientEmail, username, reservationCode, startDate, endDate, vehicleBrand, vehicleYear, vehicleColor, seater, childSeatsNumber, vehicleModel, countTime, priceHour, totalPrice, location, imageUrl);

            verify(templateEngine).process(eq("reservation-email"), any(Context.class));
        }

        @Test
        @DisplayName("Should handle exception when sending confirmation email fails")
        void sendConfirmationEmail_WhenExceptionThrown_ShouldHandleGracefully() {

            when(templateEngine.process(eq("reservation-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Mail server error"));

            assertDoesNotThrow(() -> emailService.sendConfirmationEmail(recipientEmail, username, reservationCode, startDate, endDate, vehicleBrand, vehicleYear, vehicleColor, seater, childSeatsNumber, vehicleModel, countTime, priceHour, totalPrice, location, imageUrl));

            verify(mailSender).createMimeMessage();
            verify(mailSender, never()).send(any(MimeMessage.class));
        }

        @Test
        @DisplayName("Should send confirmation email with zero child seats")
        void sendConfirmationEmail_WithZeroChildSeats_ShouldSendSuccessfully() {

            when(templateEngine.process(eq("reservation-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendConfirmationEmail(recipientEmail, username, reservationCode, startDate, endDate, vehicleBrand, vehicleYear, vehicleColor, seater, 0, vehicleModel, countTime, priceHour, totalPrice, location, imageUrl);

            ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
            verify(templateEngine).process(eq("reservation-email"), contextCaptor.capture());

            Context capturedContext = contextCaptor.getValue();
            assertEquals(0, capturedContext.getVariable("childSeatsNumber"));

            verify(mailSender).send(mimeMessage);
        }
    }

    @Nested
    @DisplayName("sendHtmlEmail() integration tests")
    class SendHtmlEmailTests {

        @Test
        @DisplayName("Should send HTML email with correct sender information")
        void sendHtmlEmail_ShouldSetCorrectFromAddress() throws Exception {

            when(templateEngine.process(eq("welcome-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendRegistrationEmail(recipientEmail, username);

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        @DisplayName("Should process HTML content through template engine")
        void sendHtmlEmail_ShouldProcessTemplateCorrectly() {

            String customHtml = "<html><body><h1>Custom Content</h1></body></html>";
            when(templateEngine.process(eq("welcome-email"), any(Context.class))).thenReturn(customHtml);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendRegistrationEmail(recipientEmail, username);

            verify(templateEngine).process(eq("welcome-email"), any(Context.class));
            verify(mailSender).send(mimeMessage);
        }

        @Test
        @DisplayName("Should handle null recipient gracefully")
        void sendHtmlEmail_WhenNullRecipient_ShouldHandleGracefully() {

            when(templateEngine.process(eq("welcome-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            assertDoesNotThrow(() -> emailService.sendRegistrationEmail(null, username));
        }
    }

    @Nested
    @DisplayName("resetPasswordEmailEmail() tests")
    class resetPasswordEmailTests {

        @Test
        @DisplayName("should send reset password email successfully with correct template, username, and resetLink")
        void sendResetPasswordEmail_WhenValidData_ShouldSendEmailSuccessfully() throws Exception {
            String token = "sEcReT_tOkEn_123";
            String expectedResetLink = "http://localhost:5173/reset-password?token=" + token;

            when(templateEngine.process(eq("forgot-password-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendResetPasswordEmail(recipientEmail, username, token);

            ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
            verify(templateEngine).process(eq("forgot-password-email"), contextCaptor.capture());

            Context capturedContext = contextCaptor.getValue();
            assertEquals(username, capturedContext.getVariable("username"));
            assertEquals(expectedResetLink, capturedContext.getVariable("resetLink"));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        @DisplayName("Should use correct template name for reset password email")
        void sendResetPasswordEmail_ShouldUseCorrectTemplate() {
            String token = "anyToken";
            when(templateEngine.process(eq("forgot-password-email"), any(Context.class))).thenReturn(htmlContent);
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendResetPasswordEmail(recipientEmail, username, token);

            verify(templateEngine).process(eq("forgot-password-email"), any(Context.class));
        }

    }
}