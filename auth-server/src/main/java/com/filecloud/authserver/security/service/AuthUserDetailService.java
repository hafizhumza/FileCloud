package com.filecloud.authserver.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.filecloud.authserver.model.db.AuthUser;
import com.filecloud.authserver.model.db.Role;
import com.filecloud.authserver.repository.UserRepository;
import com.filecloud.authserver.security.dto.AuthUserDetail;
import com.filecloud.authserver.util.Util;


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

		AuthUserDetail customUserDetail = new AuthUserDetail(authUser.getId(), authUser.getFullName(), authUser.getEmail(), authUser.getPassword(), authUser.isEnabled(), authUser.isAccountNonExpired(), authUser.isCredentialsNonExpired(), authUser.isAccountNonLocked(), getAuthorities(authUser.getRoles()));
		return customUserDetail;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
		if (!Util.isValidList(roles))
			return null;

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		roles.forEach(role -> {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
			role.getPermissions().forEach(permission -> {
				grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
			});
		});

		return grantedAuthorities;
	}
}
