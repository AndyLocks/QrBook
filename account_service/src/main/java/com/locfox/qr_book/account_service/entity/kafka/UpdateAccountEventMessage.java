package com.locfox.qr_book.account_service.entity.kafka;

import com.locfox.qr_book.account_service.entity.Account;

public record UpdateAccountEventMessage(Long id, String nickname, Account newData) {
}
