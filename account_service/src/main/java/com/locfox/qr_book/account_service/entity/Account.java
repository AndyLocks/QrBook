package com.locfox.qr_book.account_service.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    /// Creates an example for query based on `nickname` ignoring null values.
    ///
    /// @param nickname cant be `null`
    /// @throws IllegalArgumentException if `nickname` is `null`
    public static Example<Account> nicknameExample(String nickname) {
        return Example.of(Account.builder().nickname(Assert.notNull(nickname, "Nickname cannot be null")).build(), ExampleMatcher.matchingAll().withIgnoreNullValues());
    }

    @Override
    public String getUsername() {
        return this.getId().toString();
    }

    @Override
    public String getPassword() {
        return this.getPasswordHash();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public static class AccountBuilder {
        private Long id = null;
        private String nickname = null;
        private String passwordHash = null;

        public AccountBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AccountBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public AccountBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Account build() {
            return new Account(id, nickname, passwordHash);
        }

    }

    public Account() {
    }

    public Account(Long id, String nickname, String passwordHash) {
        this.id = id;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }

}
