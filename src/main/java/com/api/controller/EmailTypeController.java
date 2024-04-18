package com.api.controller;

import com.api.component.CustomLogger;
import com.api.dto.DomainDTO;
import com.api.dto.MessageDTO;
import com.api.service.EmailTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class EmailTypeController {
    private final CustomLogger logger;
    private static final String SUCCESS_MSG = "Success";
    private final EmailTypeService service;

    public EmailTypeController(CustomLogger logger, EmailTypeService service) {
        this.logger = logger;
        this.service = service;
    }

    @PostMapping("/addDomain")
    public ResponseEntity<MessageDTO> addDomain(@RequestParam String text) {
        logger.logInfo("POST : addDomain");
        service.addDomain(text);
        return ResponseEntity.ok(new MessageDTO(SUCCESS_MSG));
    }

    @GetMapping("/getDomains")
    public ResponseEntity<List<DomainDTO>> getDomains() {
        logger.logInfo("GET : getDomain");
        try {
            List<DomainDTO> list = service.getDomains();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PutMapping("/updateDomainById")
    public ResponseEntity<MessageDTO> updateDomainById(@RequestParam Long id, String newDomain) {
        logger.logInfo("PUT : updateDomainById");
        service.updateDomain(id, newDomain);
        return ResponseEntity.ok(new MessageDTO(SUCCESS_MSG));
    }

    @PutMapping("/updateDomainByName")
    public ResponseEntity<MessageDTO> updateDomainByName(@RequestParam String domain, String newEmail) {
        logger.logInfo("PUT : updateDomainByName");
        service.updateDomain(domain, newEmail);
        return ResponseEntity.ok(new MessageDTO(SUCCESS_MSG));
    }

    @DeleteMapping("/deleteDomainById")
    public ResponseEntity<MessageDTO> deleteDomainById(@RequestParam Long domainId) {
        logger.logInfo("DELETE : deleteDomainById");
        service.deleteDomain(domainId);
        return ResponseEntity.ok(new MessageDTO("Domain deleted successfully"));
    }

    @DeleteMapping("/deleteDomainByName")
    public ResponseEntity<MessageDTO> deleteDomainByName(@RequestParam String domain) {
        logger.logInfo("DELETE : deleteDomainByName");
        service.deleteDomain(domain);
        return ResponseEntity.ok(new MessageDTO("Domain deleted successfully"));
    }
}
