package com.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "email")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "type_email_id", nullable = false)


    private Domain typeEmail;

    public Email(String email, Domain domain) {
        this.name = email;
        this.typeEmail = domain;
    }

    public Email(Long id, String email) {
        this.id = id;
        this.name = email;
    }

    public Email() {
    }

    public Email(String email) {
        this.name = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return name;
    }

    public void setEmail(String email) {
        this.name = email;
    }

    public Domain getEmailTypeEntity() {
        return typeEmail;
    }

    public void setEmailTypeEntity(Domain domain) {
        this.typeEmail = domain;
    }
}

