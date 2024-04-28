package com.api.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "email_type")
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "domain")
    private String domain;

    @OneToMany(mappedBy = "typeEmail", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Email> emails;

    public Domain(String domain) {
        this.domain = domain;
    }

    public Domain() {
    }

    public Domain(String domain, List<Email> emails) {
        this.domain = domain;
        this.emails = emails;
    }

    public Domain(Long id, String domain) {
        this.id = id;
        this.domain = domain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long typeEmailId) {
        this.id = typeEmailId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}