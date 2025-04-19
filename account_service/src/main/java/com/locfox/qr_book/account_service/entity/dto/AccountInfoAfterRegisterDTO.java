package com.locfox.qr_book.account_service.entity.dto;

import com.locfox.qr_book.account_service.entity.Account;

public record AccountInfoAfterRegisterDTO(Long id, String nickname) {
    public static AccountInfoAfterRegisterDTO from(Account account) {
        return new AccountInfoAfterRegisterDTO(account.getId(), account.getNickname());
    }
}
