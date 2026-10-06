package com.baraigad.birthday.service;

import com.baraigad.volunteer.service.BirthdayEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Backward-compatible entry point for the combined birthday notification flow. */
@Service
@RequiredArgsConstructor
public class BirthdayServiceImpl implements BirthdayService {
    private final BirthdayEmailService birthdayEmailService;

    @Override
    public void sendBirthdayMessages() {
        birthdayEmailService.sendBirthdayEmails();
    }
}
