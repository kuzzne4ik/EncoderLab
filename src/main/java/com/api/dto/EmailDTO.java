package com.api.dto;

public class EmailDTO {
    int id;
    String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EmailDTO(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public EmailDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
