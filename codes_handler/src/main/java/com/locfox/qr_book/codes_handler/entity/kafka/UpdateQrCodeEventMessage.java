package com.locfox.qr_book.codes_handler.entity.kafka;

import com.locfox.qr_book.codes_handler.entity.QrCode;

import java.util.UUID;

public record UpdateQrCodeEventMessage(UUID uuid, QrCode newQrCode) {
}
