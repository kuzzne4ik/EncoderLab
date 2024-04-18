package com.api.dao;

import com.api.entity.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTypeRepository extends JpaRepository<EmailType, Long> {
    EmailType findByDomain(String emailType);
}
