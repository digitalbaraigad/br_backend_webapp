package com.baraigad.test;

import com.baraigad.birthday.service.BirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BirthdayTestController {

    private final BirthdayService birthdayService;

    @GetMapping("/test/birthday")
    public String testBirthday() {

        birthdayService.sendBirthdayMessages();

        return "Birthday messages sent";
    }
}