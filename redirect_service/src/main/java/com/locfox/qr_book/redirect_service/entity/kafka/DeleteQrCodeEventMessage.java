package com.locfox.qr_book.redirect_service.entity.kafka;

import java.util.UUID;

public record DeleteQrCodeEventMessage(UUID crCodeId) {
    public static DeleteQrCodeEventMessage from(UUID crCodeId) {
        return new DeleteQrCodeEventMessage(crCodeId);
    }
}
