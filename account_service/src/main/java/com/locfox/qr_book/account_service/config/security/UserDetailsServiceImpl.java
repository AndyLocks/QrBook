package com.locfox.qr_book.account_service.config.security;

import com.locfox.qr_book.account_service.repository.AccountCacheableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private AccountCacheableRepository repository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(AccountCacheableRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long id = null;

        try {
            id = Long.valueOf(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("User [" + username + "] was not found");
        }

        return repository.find(id).orElseThrow(()-> new UsernameNotFoundException("User [" + username + "] was not found"));
    }

}
