package com.internship.tool.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to) {
        System.out.println("Email sent to " + to);
    }
}
