package com.locfox.qr_book.account_service.repository;

import com.locfox.qr_book.account_service.config.kafka.Topics;
import com.locfox.qr_book.account_service.config.redis.templates.AccountTemplate;
import com.locfox.qr_book.account_service.entity.Account;
import com.locfox.qr_book.account_service.entity.kafka.DeleteAccountEventMessage;
import com.locfox.qr_book.account_service.entity.kafka.UpdateAccountEventMessage;
import com.locfox.qr_book.account_service.repository.jpa.AccountsJpaRepository;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountCacheableRepository {

    private RedisTemplate<String, Account> accountRedisTemplate;
    private AccountsJpaRepository jpaRepository;
    private KafkaTemplate<String, DeleteAccountEventMessage> deleteAccountEventMessageKafkaTemplate;
    private KafkaTemplate<String, UpdateAccountEventMessage> updateAccountEventMessageKafkaTemplate;
    private final Logger LOG = LoggerFactory.getLogger(AccountCacheableRepository.class);

    @Autowired
    public AccountCacheableRepository(RedisTemplate<String, Account> accountRedisTemplate, AccountsJpaRepository jpaRepository, KafkaTemplate<String, DeleteAccountEventMessage> deleteAccountEventMessageKafkaTemplate, KafkaTemplate<String, UpdateAccountEventMessage> updateAccountEventMessageKafkaTemplate) {
        this.accountRedisTemplate = accountRedisTemplate;
        this.jpaRepository = jpaRepository;
        this.deleteAccountEventMessageKafkaTemplate = deleteAccountEventMessageKafkaTemplate;
        this.updateAccountEventMessageKafkaTemplate = updateAccountEventMessageKafkaTemplate;
    }

    /// Creates an account in the database and returns the created account
    ///
    /// @param account cant be `null`
    /// @return the saved account. Will never be `null`.
    /// @throws IllegalArgumentException if `account` is null
    public Account createAccount(Account account) {
        var acc = jpaRepository.save(Assert.notNull(account, "Account cant be null"));
        LOG.debug("New account was created: [{}]", account);
        return acc;
    }

    /// Clears cache and updates account information in the database
    ///
    /// @param userId       cannot be null
    /// @param nickname     new nickname; cannot be `null`
    /// @param passwordHash new password hash; cannot be `null`
    /// @throws IllegalArgumentException if `userId`, `nickname` or `passwordHash` is `null`
    public void updateData(Long userId, String nickname, String passwordHash) {
        jpaRepository.findById(Assert.notNull(userId, "User id cannot be null")).ifPresent(a -> {
            accountRedisTemplate.delete(a.getNickname());
            accountRedisTemplate.delete(a.getId().toString());
            accountRedisTemplate.delete(Assert.notNull(nickname, "Nickname cannot be null"));

            updateAccountEventMessageKafkaTemplate.send(Topics.UPDATE_ACCOUNT_EVENT_MESSAGE.getName(),
                    new UpdateAccountEventMessage(a.getId(), a.getNickname(), Account.builder()
                            .id(a.getId())
                            .nickname(nickname)
                            .passwordHash(Assert.notNull(passwordHash, "Password hash cannot be null"))
                            .build()));

            a.setNickname(nickname);
            a.setPasswordHash(passwordHash);

            jpaRepository.save(a);
        });
    }

    /// Creates an account in the database and returns the created account
    ///
    /// @param nickname     cant be `null`
    /// @param passwordHash cant be `null`
    /// @return the saved account. Will never be `null`.
    /// @throws IllegalArgumentException if `nickname` or `passwordHash` is `null`
    public Account createAccount(String nickname, String passwordHash) {
        return this.createAccount(Account.builder()
                .nickname(Assert.hasText(nickname, "Nickname cannot be null"))
                .passwordHash(Assert.hasText(passwordHash, "Password hash cannot be null"))
                .build());
    }

    /// Deletes account from redis and database
    ///
    /// @param id cant be null
    /// @throws IllegalArgumentException if id is null
    public void deleteAccount(Long id) {
        var account = find(Assert.notNull(id, "Id cannot be null"));

        if (account.isEmpty()) return;

        accountRedisTemplate.delete(account.get().getNickname());
        accountRedisTemplate.delete(account.get().getId().toString());
        jpaRepository.deleteById(id);
        deleteAccountEventMessageKafkaTemplate.send(Topics.DELETE_ACCOUNT_EVENT_MESSAGE.getName(),
                new DeleteAccountEventMessage(id, account.get().getNickname()));
    }

    /// Returns [Account] from the database or redis found by the `nickname`
    ///
    /// @param nickname cant be null
    /// @return `account` from database or redis
    /// @throws IllegalArgumentException if nickname is null
    public Optional<Account> find(String nickname) {
        LOG.debug("Finding an account by nickname [{}]", nickname);
        var account = accountRedisTemplate.opsForValue().get(Assert.notNull(nickname, "Nickname cannot be null"));
        LOG.debug("Account from redis: [{}]", account);

        if (account == null) {
            LOG.debug("Cache is null. Searching in the database...");
            var jpaAccount = jpaRepository.findOne(Account.nicknameExample(nickname));
            LOG.debug("Account from database: [{}]", jpaAccount);

            if (jpaAccount.isEmpty()) return Optional.empty();

            accountRedisTemplate.opsForValue().set(jpaAccount.get().getId().toString(), jpaAccount.get(), AccountTemplate.TTL_DURATION);
            accountRedisTemplate.opsForValue().set(jpaAccount.get().getNickname(), jpaAccount.get(), AccountTemplate.TTL_DURATION);

            LOG.debug("Cache was saved: [{}]", jpaAccount);

            return jpaAccount;
        }

        accountRedisTemplate.expire(nickname, AccountTemplate.TTL_DURATION);
        accountRedisTemplate.expire(account.getId().toString(), AccountTemplate.TTL_DURATION);

        return Optional.of(account);
    }

    /// Returns [Account] from the database or redis found by the `id`
    ///
    /// @param id cant be `null`
    /// @return `account` from database or redis
    /// @throws IllegalArgumentException if nickname is null
    public Optional<Account> find(Long id) {
        LOG.debug("Finding an account by id [{}]", id);
        var account = accountRedisTemplate.opsForValue().get(Assert.notNull(id, "Id cannot be null").toString());
        LOG.debug("Account from redis: [{}]", account);

        if (account == null) {
            LOG.debug("Cache is null. Searching in the database...");
            var jpaAccount = jpaRepository.findById(id);
            LOG.debug("Account from database: [{}]", jpaAccount);

            if (jpaAccount.isEmpty()) return Optional.empty();

            accountRedisTemplate.opsForValue().set(jpaAccount.get().getId().toString(), jpaAccount.get(), AccountTemplate.TTL_DURATION);
            accountRedisTemplate.opsForValue().set(jpaAccount.get().getNickname(), jpaAccount.get(), AccountTemplate.TTL_DURATION);

            LOG.debug("Cache was saved: [{}]", jpaAccount);

            return jpaAccount;
        }

        accountRedisTemplate.expire(id.toString(), AccountTemplate.TTL_DURATION);
        accountRedisTemplate.expire(account.getNickname(), AccountTemplate.TTL_DURATION);

        return Optional.of(account);
    }

}
