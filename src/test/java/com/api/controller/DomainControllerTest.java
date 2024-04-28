package com.api.controller;

import com.api.component.CustomLogger;
import com.api.dto.DomainDTO;
import com.api.dto.MessageDTO;
import com.api.service.DomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DomainControllerTest {

    @Mock
    private CustomLogger logger;

    @Mock
    private DomainService service;

    @InjectMocks
    private DomainController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddDomain() {
        String domain = "example.com";
        MessageDTO messageDTO = new MessageDTO("Success");
        ResponseEntity<MessageDTO> expectedResponse = ResponseEntity.ok(messageDTO);

        ResponseEntity<MessageDTO> response = controller.addDomain(domain);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        verify(logger).logInfo("POST : addDomain");
        verify(service).addDomain(domain);
    }

    @Test
    void testGetDomains() {
        List<DomainDTO> domainList = new ArrayList<>();
        when(service.getDomains()).thenReturn(domainList);

        ResponseEntity<List<DomainDTO>> response = controller.getDomains();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(domainList, response.getBody());
        verify(logger).logInfo("GET : getDomain");
    }

    @Test
    void testUpdateDomainById() {
        Long id = 1L;
        String newDomain = "newexample.com";
        MessageDTO messageDTO = new MessageDTO("Success");
        ResponseEntity<MessageDTO> expectedResponse = ResponseEntity.ok(messageDTO);

        ResponseEntity<MessageDTO> response = controller.updateDomainById(id, newDomain);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        verify(logger).logInfo("PUT : updateDomainById");
        verify(service).updateDomain(id, newDomain);
    }

    @Test
    void testUpdateDomainByName() {
        String domain = "example.com";
        String newDomain = "newexample.com";
        MessageDTO messageDTO = new MessageDTO("Success");
        ResponseEntity<MessageDTO> expectedResponse = ResponseEntity.ok(messageDTO);

        ResponseEntity<MessageDTO> response = controller.updateDomainByName(domain, newDomain);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        verify(logger).logInfo("PUT : updateDomainByName");
        verify(service).updateDomain(domain, newDomain);
    }

    @Test
    void testDeleteDomainById() {
        Long domainId = 1L;
        MessageDTO messageDTO = new MessageDTO("Domain deleted successfully");
        ResponseEntity<MessageDTO> expectedResponse = ResponseEntity.ok(messageDTO);

        ResponseEntity<MessageDTO> response = controller.deleteDomainById(domainId);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        verify(logger).logInfo("DELETE : deleteDomainById");
        verify(service).deleteDomain(domainId);
    }

    @Test
    void testDeleteDomainByName() {
        String domain = "example.com";
        MessageDTO messageDTO = new MessageDTO("Domain deleted successfully");
        ResponseEntity<MessageDTO> expectedResponse = ResponseEntity.ok(messageDTO);

        ResponseEntity<MessageDTO> response = controller.deleteDomainByName(domain);

        assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());
        verify(logger).logInfo("DELETE : deleteDomainByName");
        verify(service).deleteDomain(domain);
    }
}
