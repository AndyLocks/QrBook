package com.locfox.qr_book.account_service.config.kafka;

/// Kafka topics
public enum Topics {

    UPDATE_ACCOUNT_EVENT_MESSAGE("update_account_events"),
    DELETE_ACCOUNT_EVENT_MESSAGE("delete_account_events");
    private final String name;

    Topics(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
