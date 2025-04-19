package com.locfox.qr_book.account_service.entity.dto;

import com.locfox.qr_book.account_service.entity.Account;

public record AccountInfoDTO(String nickname) {

    public static AccountInfoDTO from(final Account account) {
        return new AccountInfoDTO(account.getNickname());
    }

}
