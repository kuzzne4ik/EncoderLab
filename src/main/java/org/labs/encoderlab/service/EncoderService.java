package org.labs.encoderlab.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EncoderService {
    public List<String> findEmailAddresses(String text) {
        List<String> emailAddresses = new ArrayList<>();

        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            emailAddresses.add(matcher.group());
        }

        return emailAddresses;
    }

    public List<String> findPhoneNumbers(String text) {
        List<String> phoneNumbers = new ArrayList<>();

        String phoneRegex = "\\b\\d{3}-\\d{3}-\\d{4}\\b";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            phoneNumbers.add(matcher.group());
        }

        return phoneNumbers;
    }

    public String removeEmailAddressesAndPhoneNumbers(String text) {

        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        text = text.replaceAll(emailRegex, "");

        String phoneRegex = "\\b\\d{3}-\\d{3}-\\d{4}\\b";
        text = text.replaceAll(phoneRegex, "");

        return text;
    }
}
