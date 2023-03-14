package com.loans.model;

import java.util.StringJoiner;

public class NotificationDTO {
    private final String message;

    public NotificationDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", NotificationDTO.class.getSimpleName() + "[", "]")
                .add("message='" + message + "'")
                .toString();
    }
}
