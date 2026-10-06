package com.baraigad.scheduler;

import com.baraigad.birthday.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BirthdayWhatsAppScheduler {

    private final BirthdayService birthdayService;

    @Scheduled(
            cron = "${baraigad.birthday-notification.cron:0 0 9 * * *}",
            zone = "${baraigad.birthday-notification.zone:Asia/Kolkata}"
    )
    public void sendBirthdayMessages() {

        log.info("Birthday Scheduler Started");

        birthdayService.sendBirthdayMessages();

        log.info("Birthday Scheduler Completed");
    }
}
