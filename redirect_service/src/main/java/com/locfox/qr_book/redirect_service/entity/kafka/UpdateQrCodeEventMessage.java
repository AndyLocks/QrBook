package com.locfox.qr_book.redirect_service.entity.kafka;

import java.util.UUID;

public record UpdateQrCodeEventMessage(UUID uuid, QrCode newQrCode) {
}
