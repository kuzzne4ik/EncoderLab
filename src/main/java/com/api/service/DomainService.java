package com.api.service;

import com.api.component.Cache;
import com.api.component.CustomLogger;
import com.api.dao.EmailRepository;
import com.api.dao.EmailTypeRepository;
import com.api.dto.DomainDTO;
import com.api.entity.Email;
import com.api.entity.DomainEntity;
import com.api.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailTypeService {
    private final EmailRepository emailRepository;
    private static final String EXCEPTION_MSG = "Domain has slave emails";
    private final Cache cache;
    private final CustomLogger customLogger;
    private final EmailTypeRepository emailTypeRepository;

    public EmailTypeService(EmailRepository emailRepository, Cache cache, CustomLogger customLogger, EmailTypeRepository emailTypeRepository) {
        this.emailRepository = emailRepository;
        this.cache = cache;
        this.customLogger = customLogger;
        this.emailTypeRepository = emailTypeRepository;
    }

    @Transactional
    public void updateDomain(Long id, String newDomain) {

        Optional<DomainEntity> emailTypeEntityOptional = emailTypeRepository.findById(id);

        if (emailTypeEntityOptional.isPresent() && newDomain != null && !newDomain.isEmpty() && checkDomain(newDomain)) {
            cache.remove(emailTypeEntityOptional.get().getDomain());
            List<Email> emails = emailTypeEntityOptional.get().getEmails();
            if (!emails.isEmpty()) {
                customLogger.logError(EXCEPTION_MSG);
                throw new ServiceException();
            }
            Iterator<Email> iterator = emails.iterator();
            while (iterator.hasNext()) {
                Email email = iterator.next();
                emailRepository.delete(email);
                iterator.remove();
            }

            emailTypeEntityOptional.get().setDomain(newDomain);
            customLogger.logCachePut(emailTypeEntityOptional.get().getDomain());
            emailTypeRepository.save(emailTypeEntityOptional.get());
        }

    }

    @Transactional
    public void updateDomain(String domain, String newDomain) {
        DomainEntity domainEntity = emailTypeRepository.findByDomain(domain);

        if (domainEntity != null && newDomain != null && !newDomain.isEmpty() && checkDomain(newDomain)) {
            List<Email> emails = domainEntity.getEmails();
            if (emails != null && !emails.isEmpty()) { // Добавлено условие проверки на null
                customLogger.logError(EXCEPTION_MSG);
                throw new ServiceException();
            }
            cache.remove(domainEntity.getDomain());
            // Добавлена проверка на null для emails
            if (emails != null) {
                Iterator<Email> iterator = emails.iterator();
                while (iterator.hasNext()) {
                    Email email = iterator.next();
                    emailRepository.delete(email);
                    iterator.remove();
                }
            }
            domainEntity.setDomain(newDomain);
            cache.put(domainEntity.getDomain(), domainEntity);
            emailTypeRepository.save(domainEntity);
        }
    }

    private boolean checkDomain(String text) {
        String reg = "[a-z0-9.-]+\\.[a-z]{2,}\\b";
        Pattern emailPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(text);
        return emailMatcher.find();
    }

    @Transactional
    public void addDomain(String domain) {
        if (checkDomain(domain) && cache.get(domain) == null) {
            if (emailTypeRepository.findByDomain(domain) != null) {
                customLogger.logError("Domain was in database");
                throw new ServiceException();
            }
            DomainEntity domainEntity = new DomainEntity(domain);
            emailTypeRepository.save(domainEntity);
            cache.put(domainEntity.getDomain(), domainEntity);
            customLogger.logCachePut(domainEntity.getDomain());
        } else {
            customLogger.logInfo("Is not domain or value was in cache");
            throw new ServiceException();
        }
    }

    @Transactional
    public List<DomainDTO> getDomains() {
        List<DomainEntity> domainEntities = emailTypeRepository.findAll();
        List<DomainDTO> result = new ArrayList<>();
        for (int i = 0; i < domainEntities.size(); i++) {
            cache.put(domainEntities.get(i).getDomain(), domainEntities.get(i));
            customLogger.logCachePut(domainEntities.get(i).getDomain());
            result.add(new DomainDTO(domainEntities.get(i).getId()
                    + ". " + domainEntities.get(i).getDomain()));
        }
        return result;
    }

    @Transactional
    public void deleteDomain(Long id) {
        Optional<DomainEntity> emailTypeEntity = emailTypeRepository.findById(id);
        if (emailTypeEntity.isPresent()) {
            List<Email> emails = emailTypeEntity.get().getEmails();
            if (emails != null && !emails.isEmpty()) {
                customLogger.logError(EXCEPTION_MSG);
                throw new ServiceException();
            }
            emailTypeRepository.delete(emailTypeEntity.get());
            cache.remove(emailTypeEntity.get().getDomain());
            customLogger.logCacheRemove(emailTypeEntity.get().getDomain());
        } else {
            throw new ServiceException();
        }
    }

    @Transactional
    public void deleteDomain(String name) {
        Optional<DomainEntity> optionalEmailTypeEntity;
        if (cache.get(name) != null) {
            optionalEmailTypeEntity = (Optional<DomainEntity>) cache.get(name);
            customLogger.logInfo("Value from cache");
        } else {
            optionalEmailTypeEntity = Optional.ofNullable(emailTypeRepository.findByDomain(name));
        }
        if (optionalEmailTypeEntity.isPresent()) {
            DomainEntity domainEntityEntity = optionalEmailTypeEntity.get();
            List<Email> emails = domainEntityEntity.getEmails();
            if (emails != null && !emails.isEmpty()) {
                customLogger.logError(EXCEPTION_MSG);
                throw new ServiceException();
            }
            customLogger.logCacheRemove(domainEntityEntity.getDomain());
            emailTypeRepository.delete(domainEntityEntity);
            cache.remove(domainEntityEntity.getDomain());
        } else {
            throw new ServiceException();
        }
    }

}
