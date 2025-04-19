package com.locfox.qr_book.codes_handler.repository.jpa;

import com.locfox.qr_book.codes_handler.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QrCodeJpaRepository extends JpaRepository<QrCode, UUID> {
}
