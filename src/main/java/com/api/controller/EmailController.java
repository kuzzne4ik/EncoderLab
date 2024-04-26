package com.api.controller;

import com.api.component.CustomLogger;
import com.api.dto.EmailDTO;
import com.api.dto.MessageDTO;
import com.api.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class EmailController {
    private static final String SUCCESS_MSG = "Success";
    private final CustomLogger logger;
    private final EmailService service;

    public EmailController(CustomLogger logger, EmailService service) {
        this.logger = logger;
        this.service = service;
    }

    @PostMapping("/processUrl")
    public ResponseEntity<MessageDTO> processUrl(@RequestParam String text) {
        logger.logInfo("POST : processUrl");
        String processedText = service.getConfidentialText(text);
        MessageDTO dto = new MessageDTO(processedText);
        if (dto.getText() == null) {
            dto.setText("null");
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getEmails")
    public ResponseEntity<List<String>> getEmails(@RequestParam String text) {
        List<String> emailList = service.getEmails(text);
        return ResponseEntity.ok(emailList);
    }

    @GetMapping("/getEmailsByDomain")
    public ResponseEntity<List<EmailDTO>> getEmailsByDomain(@RequestParam String text) {
        logger.logInfo("GET : getEmailsByDomain");
        List<EmailDTO> emailList = service.getEmailsByEmailType(text);
        return ResponseEntity.ok(emailList);
    }

    @PutMapping("/updateEmailById")
    public ResponseEntity<MessageDTO> updateEmailById(@RequestParam Long id, String newDomain) {
        logger.logInfo("PUT : updateEmailsById");
        service.updateEmail(id, newDomain);
        return ResponseEntity.ok(new MessageDTO(SUCCESS_MSG));
    }

    @PutMapping("/updateEmailByName")
    public ResponseEntity<MessageDTO> updateEmailByName(@RequestParam String email, String newEmail) {
        logger.logInfo("PUT : updateEmailsByName");
        service.updateEmail(email, newEmail);
        return ResponseEntity.ok(new MessageDTO(SUCCESS_MSG));
    }

    @DeleteMapping("/deleteEmailById")
    public ResponseEntity<MessageDTO> deleteEmailById(@RequestParam Long emailId) {
        logger.logInfo("DELETE : deleteEmailsById");
        service.deleteEmail(emailId);
        return ResponseEntity.ok(new MessageDTO("Email deleted successfully"));
    }

    @DeleteMapping("/deleteEmailByName")
    public ResponseEntity<MessageDTO> deleteEmailByName(@RequestParam String email) {
        logger.logInfo("DELETE : deleteEmailsByName");
        service.deleteEmail(email);
        return ResponseEntity.ok(new MessageDTO("Email deleted successfully"));
    }
}