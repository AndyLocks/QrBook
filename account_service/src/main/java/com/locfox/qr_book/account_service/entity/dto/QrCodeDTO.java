package com.locfox.qr_book.account_service.entity.dto;

import com.locfox.qr_book.Services;

public record QrCodeDTO(String uuid, String url) {
    public static QrCodeDTO from(Services.QrCode qrCode) {
        return new QrCodeDTO(qrCode.getUuid(), qrCode.getUrl());
    }
}
