package vn.hoidanit.jobhunter.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private SubscriberService subscriberService;
    private final EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendConfirmationEmail() throws MessagingException {
//        this.emailService.sendSimpleEmail();
//        this.emailService.sendEmailSync("datnv621@wru.vn", "Testing from Spring Boot", "<h1><b>Hello</b></h1>", false, true);
//        this.emailService.sendEmailFromTemplateSync("datnv621@wru.vn", "Testing from Spring Boot","job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "OK";
    }
}
