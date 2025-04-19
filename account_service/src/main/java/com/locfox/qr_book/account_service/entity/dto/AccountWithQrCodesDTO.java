package com.locfox.qr_book.account_service.entity.dto;

import java.util.List;

public record AccountWithQrCodesDTO(String nickname, List<QrCodeDTO> codes) {
}
