package com.api.dao;

import com.api.entity.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTypeRepository extends JpaRepository<DomainEntity, Long> {
    DomainEntity findByDomain(String emailType);
}
