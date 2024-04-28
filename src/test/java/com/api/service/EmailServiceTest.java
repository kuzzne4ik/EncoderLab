
package com.api.service;

import com.api.component.Cache;
import com.api.component.CustomLogger;
import com.api.dao.DomainRepository;
import com.api.dao.EmailRepository;
import com.api.dto.EmailDTO;
import com.api.entity.Domain;
import com.api.entity.Email;
import com.api.exceptions.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    DomainRepository emailTypeRepository;

    @Mock
    Cache cache;

    @Mock
    EmailRepository emailRepository;

    @Mock
    CustomLogger customLogger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testReturnAllEmails() {
        when(emailRepository.findAll()).thenReturn(getEmails());
        List<String> res = emailService.getEmails("all");
        assertEquals(3, res.size());
        Assertions.assertNotNull(res.get(0));
    }

    @Test
    void testReturnProcessedText() {
        String text1 = "rooror@mail.com give+375292556867";
        String text2 = "rooror@mail.com give";
        String text3 = "give+375292556867";
        String text4 = "give";
        assertEquals(" give", emailService.getConfidentialText(text1));
        assertEquals(" give", emailService.getConfidentialText(text2));
        assertEquals("give", emailService.getConfidentialText(text3));
        assertEquals("give", emailService.getConfidentialText(text4));
    }

    @Test
    void testReturnEmailsByEmailType() {
        List<Email> list = new ArrayList<>();
        list.add(new Email(1L, "gagagga@mail.ru"));
        list.add(new Email(2L, "wqeqe@mail.ru"));
        when(emailRepository.findByEmailTypeDomain("mail.ru")).thenReturn(list);
        List<EmailDTO> emailDTOList = new ArrayList<>();
        emailDTOList = emailService.getEmailsByEmailType("mail.ru");
        Assertions.assertNotNull(emailDTOList);
        assertEquals(2,emailDTOList.size());
        emailDTOList.get(1).setText("weq@mail.com");
        assertEquals("weq@mail.com",emailDTOList.get(1).getText());
    }

    @Test
    void testReturnUpdateEmailsByName() {
        String oldEmail = "old@example.com";
        String newEmail = "new@example.com";
        Email emailEntity = new Email(oldEmail);
        Domain emailType = new Domain("example.com");

        when(emailRepository.findByName(oldEmail)).thenReturn(emailEntity);
        when(emailTypeRepository.findByDomain("example.com")).thenReturn(emailType);

        emailService.updateEmail(oldEmail, newEmail);

        verify(emailRepository, Mockito.times(1)).findByName(Mockito.any());
    }

    @Test
    void testReturnUpdateEmailsById() {
        String oldEmail = "old@example.com";
        String newEmail = "new@example.com";
        Domain emailType = new Domain("example.com");

        when(emailRepository.findById(1L)).thenReturn(Optional.of(new Email(1L, oldEmail)));
        when(emailTypeRepository.findByDomain("example.com")).thenReturn(emailType);

        emailService.updateEmail(1L, newEmail);

        verify(emailRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testReturnUpdateEmailsByIdWithoutDomain() {
        String oldEmail = "old@example.com";
        String newEmail = "new@example.com";
        Domain emailType = new Domain("example.com");

        when(emailRepository.findById(1L)).thenReturn(Optional.of(new Email(1L, oldEmail)));
        when(emailTypeRepository.findByDomain("example.com")).thenReturn(null);

        emailService.updateEmail(1L, newEmail);

        verify(emailRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testReturnUpdateEmailsByNameWithoutDomain() {
        String oldEmail = "old@example.com";
        String newEmail = "new@example.com";
        Email emailEntity = new Email(oldEmail);
        Domain emailType = new Domain("example.com");

        when(emailRepository.findByName(oldEmail)).thenReturn(emailEntity);
        when(emailTypeRepository.findByDomain("example.com")).thenReturn(null);

        emailService.updateEmail(oldEmail, newEmail);

        verify(emailRepository, Mockito.times(1)).findByName(Mockito.any());
    }

    @Test
    void testDeleteEmailEmailExistsSuccess() {
        Long id = 1L;
        String email = "test@example.com";
        Email emailEntity = new Email(email);

        when(emailRepository.findById(id)).thenReturn(Optional.of(emailEntity));

        emailService.deleteEmail(id);

        verify(emailRepository).findById(id);
        verify(cache).remove(email);
        verify(customLogger).logCacheRemove(email);
        verify(emailRepository).delete(emailEntity);
    }

    @Test
    void testDeleteEmailEmailNotFoundErrorLogged() {
        Long id = 1L;

        when(emailRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> emailService.deleteEmail(id));

        verify(emailRepository).findById(id);
        verify(customLogger).logError("Email is not found");
        verifyNoMoreInteractions(cache);
        verifyNoMoreInteractions(emailRepository);
    }
    private List<Email> getEmails(){
        List<Email> list = new ArrayList<>();
        list.add(new Email(1L,"vafda@gmail.com"));
        list.add(new Email(2L,"vafrteda@mail.com"));
        list.add(new Email(3L,"vafda@rambler.ru"));
        return list;
    }
    @Test
    void testPutEmail() {
        EmailDTO email = new EmailDTO("test@example.com",1);
        when(emailRepository.findByName(email.getText())).thenReturn(null);

        emailService.putEmail(email);

        verify(emailRepository).findByName(email.getText());
        verify(emailRepository).save(any(Email.class));
    }

    @Test
    void testCreateOrRetrieveEmailType_EmailTypeExistsInCache() {
        String domain = "example.com";
        Domain emailType = new Domain();
        when(cache.get(domain)).thenReturn(emailType);

        Domain result = emailService.createOrRetrieveEmailType("test@" + domain);

        assertEquals(emailType, result);
    }

    @Test
    void testCreateOrRetrieveEmailType_EmailTypeExistsInRepository() {
        // Arrange
        String domain = "example.com";
        Domain emailType = new Domain();
        when(cache.get(domain)).thenReturn(null);
        when(emailTypeRepository.findByDomain(domain)).thenReturn(emailType);

        // Act
        Domain result = emailService.createOrRetrieveEmailType("test@" + domain);

        // Assert
        assertEquals(emailType, result);
        verify(cache).put(domain, emailType);
    }

    @Test
    void testCreateOrRetrieveEmailType_EmailTypeDoesNotExist() {
        String domain = "example.com";
        when(cache.get(domain)).thenReturn(null);
        when(emailTypeRepository.findByDomain(domain)).thenReturn(null);

        Domain result = emailService.createOrRetrieveEmailType("test@" + domain);

        assertEquals(domain, result.getDomain());
        verify(emailTypeRepository).save(any(Domain.class));
        verify(cache).put(domain, result);
    }

    @Test
    void testDeleteEmailEmailInCacheSuccess() {
        String email = "test@example.com";
        Email emailEntity = new Email(email);

        when(cache.get(email)).thenReturn(Optional.of(emailEntity));
    }

    @Test
    void testDeleteEmailEmailInRepositorySuccess() {
        String email = "test@example.com";
        Email emailEntity = new Email(email);

        when(emailRepository.findByName(email)).thenReturn(emailEntity);

        emailService.deleteEmail(email);

        verify(emailRepository).delete(emailEntity);
    }
    @Test
    void testDeleteEmail_EmailNotFound_ErrorLogged() {
        String email = "not_found@example.com";

        when(cache.get(email)).thenReturn(Optional.empty());
        when(emailRepository.findByName(email)).thenReturn(null);

        assertThrows(ServiceException.class, () -> emailService.deleteEmail(email));

    }

}


