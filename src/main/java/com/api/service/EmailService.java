package com.api.service;

import com.api.component.Cache;
import com.api.component.CustomLogger;
import com.api.dao.EmailRepository;
import com.api.dao.DomainRepository;
import com.api.dto.EmailDTO;
import com.api.entity.Email;
import com.api.entity.Domain;
import com.api.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailService {
    private final CustomLogger customLogger;
    private final EmailRepository emailRepository;
    private final Cache cache;
    private static final String EMAIL_REGEX = "\\b[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}\\b";
    private static String adresRegex = "[a-z0-9.-]+\\.[a-z]{2,}\\b";
    private final DomainRepository domainRepository;

    public EmailService(CustomLogger customLogger, EmailRepository emailRepository, Cache cache, DomainRepository domainRepository) {
        this.customLogger = customLogger;
        this.emailRepository = emailRepository;
        this.cache = cache;
        this.domainRepository = domainRepository;
    }

    @Transactional
    public void deleteEmail(Long id) {
        Optional<Email> optionalEmail = emailRepository.findById(id);

        if (optionalEmail.isPresent()) {
            Email email = optionalEmail.get();

            cache.remove(email.getEmail());
            customLogger.logCacheRemove(optionalEmail.get().getEmail());
            emailRepository.delete(email);
        } else {
            customLogger.logError("Email is not found");
            throw new ServiceException();
        }
    }

    @Transactional
    public void deleteEmail(String email) {
        Email emailEntity = emailRepository.findByName(email);
        if (emailEntity != null) {
            customLogger.logCacheRemove(emailEntity.getEmail());
            // Удаляем электронную почту из репозитория
            emailRepository.delete(emailEntity);
        } else {
            customLogger.logError("Email is not found");
            throw new ServiceException();
        }

    }

    @Transactional
    public void updateEmail(Long id, String newEmail) {
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(newEmail);
        if (!emailMatcher.find()) {
            customLogger.logError("Is not domain");
            throw new ServiceException();
        }

        Optional<Email> emailEntity = emailRepository.findById(id);
        if (emailEntity.isPresent()) {
            Pattern adresPattern = Pattern.compile(adresRegex, Pattern.CASE_INSENSITIVE);
            Matcher adresMatcher;
            adresMatcher = adresPattern.matcher(newEmail);
            if (adresMatcher.find()) {
                String domain = adresMatcher.group(0);
                Domain domainEntity = domainRepository.findByDomain(domain);
                if (domainEntity != null) {
                    cache.remove(emailEntity.get().getEmail());
                    cache.put(newEmail, new Email(newEmail));
                    emailEntity.get().setEmail(newEmail);
                    emailEntity.get().setEmailTypeEntity(domainEntity);
                } else {
                    domainEntity = new Domain(domain);
                    domainRepository.save(domainEntity);
                    emailEntity.get().setEmail(newEmail);
                    emailEntity.get().setEmailTypeEntity(domainEntity);
                    cache.put(domainEntity.getDomain(), domainEntity);
                }
            }
        } else {
            throw new ServiceException();
        }
    }

    @Transactional
    public void updateEmail(String email, String newEmail) {
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(newEmail);
        if (!emailMatcher.find()) {
            customLogger.logError("Is not domain");
            throw new ServiceException();
        }
        Email emailEntity = emailRepository.findByName(email);
        if (emailEntity != null) {
            Pattern adresPattern = Pattern.compile(adresRegex, Pattern.CASE_INSENSITIVE);
            Matcher adresMatcher;
            adresMatcher = adresPattern.matcher(newEmail);
            if (adresMatcher.find()) {
                String domain = adresMatcher.group(0);
                Domain domainEntity = domainRepository.findByDomain(domain);
                if (domainEntity != null) {
                    cache.remove(emailEntity.getEmail());
                    cache.put(newEmail, new Email(newEmail));
                    emailEntity.setEmail(newEmail);
                    emailEntity.setEmailTypeEntity(domainEntity);
                    customLogger.logCachePut(domainEntity.getDomain());

                } else {
                    domainEntity = new Domain(domain);
                    domainRepository.save(domainEntity);
                    emailEntity.setEmail(newEmail);
                    emailEntity.setEmailTypeEntity(domainEntity);
                    cache.put(domainEntity.getDomain(), domainEntity);
                    cache.put(emailEntity.getEmail(), emailEntity);
                    customLogger.logCachePut(domainEntity.getDomain());
                    customLogger.logCachePut(emailEntity.getEmail());
                }
            }
        } else {
            throw new ServiceException();
        }
    }

    @Transactional
    public List<String> getEmails(String text) {
        List<String> strings = new ArrayList<>();
        List<Email> emailEntities;
        if (text.equals("all")) {
            emailEntities = emailRepository.findAll();
        } else {
            throw new ServiceException();
        }
        for (int i = 0; i < emailEntities.size(); i++) {
            strings.add(emailEntities.get(i).getId().toString() + ". " + emailEntities.get(i).getEmail());
        }
        return strings;
    }

    @Transactional
    public List<EmailDTO> getEmailsByEmailType(String text) {
        List<EmailDTO> strings = new ArrayList<>();
        List<Email> emailEntities;
        emailEntities = emailRepository.findByEmailTypeDomain(text);
        for (int i = 0; i < emailEntities.size(); i++) {
            cache.put(emailEntities.get(i).getEmail(), emailEntities.get(i));
            customLogger.logCachePut(emailEntities.get(i).getEmail());
            strings.add(new EmailDTO(emailEntities.get(i).getEmail(), i));
        }
        return strings;
    }

    @Transactional
    public String getConfidentialText(String text) {

        List<String> list = new ArrayList<>();
        String phoneRegex = "\\b(?:\\+\\d{1,3}[-.\\s]?)?(\\d{1,4}[-.\\s]?){1,2}\\d{1,9}\\b";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher phoneMatcher = phonePattern.matcher(text);
        text = phoneMatcher.replaceAll("");

        Pattern emailPattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(text);

        while (emailMatcher.find()) {
            list.add(emailMatcher.group());
        }
        text = emailMatcher.replaceAll("");

        Pattern adresPattern = Pattern.compile(adresRegex, Pattern.CASE_INSENSITIVE);
        Matcher adresMatcher;

        for (String email : list) {
            adresMatcher = adresPattern.matcher(email);

            if (adresMatcher.find()) {

                String domain = adresMatcher.group();
                Domain findEntity = domainRepository.findByDomain(domain);
                if (emailRepository.findByName(email) != null) {
                    continue;
                }
                if (findEntity == null) {
                    Domain domainEntity = new Domain(domain);

                    domainRepository.save(domainEntity);

                    Email emailEntity = new Email(email, domainEntity);
                    emailRepository.save(emailEntity);
                    cache.put(emailEntity.getEmail(), emailEntity);
                    cache.put(domainEntity.getDomain(), domainEntity);
                    customLogger.logCachePut(emailEntity.getEmail());
                    customLogger.logCachePut(domainEntity.getDomain());

                } else {
                    Email emailEntity = new Email(email, findEntity);
                    emailRepository.save(emailEntity);
                    cache.put(emailEntity.getEmail(), emailEntity);
                    customLogger.logCachePut(emailEntity.getEmail());
                }

            }
        }
        return text;
    }
}