package org.labs.encoderlab.controller;
import org.labs.encoderlab.service.EncoderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EncoderController {
    private final EncoderService encoderService;

    @Autowired
    public EncoderController(EncoderService encoderService) {
        this.encoderService = encoderService;
    }

    @GetMapping("/api/encoder/filtered")
    public String getFilteredText(@RequestParam String text) {
        return encoderService.removeEmailAddressesAndPhoneNumbers(text);
    }

    @GetMapping("/api/encoder/emails")
    public List<String> getEmailAddresses(@RequestParam String text) {
        return encoderService.findEmailAddresses(text);
    }

    @GetMapping("/api/encoder/phonenumbers")
    public List<String> getPhoneNumbers(@RequestParam String text) {
        return encoderService.findPhoneNumbers(text);
    }
}