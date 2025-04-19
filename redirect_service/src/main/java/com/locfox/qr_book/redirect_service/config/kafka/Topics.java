package com.locfox.qr_book.redirect_service.config.kafka;

/// Kafka topics
public enum Topics {

    UPDATE_QR_CODE_EVENT("update_qr_code_events"),
    DELETE_QR_CODE_EVENT("delete_qr_code_events"),
    UPDATE_ACCOUNT_EVEN("update_account_events"),
    DELETE_ACCOUNT_EVENT("delete_account_events");
    private final String name;

    Topics(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
