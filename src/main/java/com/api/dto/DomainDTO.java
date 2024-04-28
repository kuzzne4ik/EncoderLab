package com.api.dto;

public class DomainDTO {
        String text;

        public DomainDTO(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
}
