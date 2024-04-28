package com.api.dao;

import com.api.entity.Email;
import com.api.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Email findByName(String email);

    @Query(value = "SELECT e FROM Email e WHERE e.typeEmail.domain = :domain")
    List<Email> findByEmailTypeDomain(@Param("domain") String domain);

    List<Email> findByTypeEmail(Domain domain);
}
