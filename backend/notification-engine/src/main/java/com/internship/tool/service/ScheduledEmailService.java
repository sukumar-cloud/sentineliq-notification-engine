package com.internship.tool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledEmailService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDailyReminder() {
        userService.getAllUsers(org.springframework.data.domain.Pageable.unpaged())
                .forEach(user -> {
                    emailService.sendEmail(
                            user.getEmail(),
                            "Daily Reminder",
                            "This is your daily reminder from Notification Engine"
                    );
                });
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void sendDeadlineAlert() {
        userService.getAllUsers(org.springframework.data.domain.Pageable.unpaged())
                .forEach(user -> {
                    emailService.sendEmail(
                            user.getEmail(),
                            "Deadline Alert",
                            "This is a deadline alert from Notification Engine"
                    );
                });
    }
}
