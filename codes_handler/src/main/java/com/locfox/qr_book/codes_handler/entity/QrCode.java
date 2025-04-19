package com.locfox.qr_book.codes_handler.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.UUID;

@Entity
@Table(name = "qr_code")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class QrCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String url;

    public QrCode() {
    }

    public QrCode(UUID id, Long ownerId, String url) {
        this.id = id;
        this.ownerId = ownerId;
        this.url = url;
    }

    /// Creates an example for query based on `ownerId` ignoring null values.
    ///
    /// @param ownerId cant be `null`
    /// @throws IllegalArgumentException if `ownerId` is `null`
    public static Example<QrCode> exampleWithOwnerId(Long ownerId) {
        return Example.of(QrCode.builder()
                        .ownerId(Assert.notNull(ownerId, "Owner id cannot be null"))
                        .build(),
                ExampleMatcher.matchingAll()
                        .withIgnoreNullValues());
    }

    public static QrCodeBuilder builder() {
        return new QrCodeBuilder();
    }

    public static class QrCodeBuilder {

        private UUID id;
        private Long ownerId;
        private String url;

        public QrCodeBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public QrCodeBuilder ownerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public QrCodeBuilder url(String url) {
            this.url = url;
            return this;
        }

        public QrCode build() {
            return new QrCode(id, ownerId, url);
        }

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
