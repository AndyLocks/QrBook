package com.locfox.qr_book.account_service.repository.jpa;

import com.locfox.qr_book.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsJpaRepository extends JpaRepository<Account, Long> {
}
