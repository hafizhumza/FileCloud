package com.filecloud.authserver.security.service;

import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.model.db.AuthUserDetail;
import com.filecloud.authserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        // Doesn't need to check explicitly. DaoAuthenticationProvider will do it for us.
        // new AccountStatusUserDetailsChecker().check(userDetails);

        return new AuthUserDetail(authUser);
    }
}
