package com.javamaster.project2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javamaster.project2.Entity.UserRole;
import com.javamaster.project2.Repository.UserRepo;



@Service
public class LoginService implements UserDetailsService {
	@Autowired
	UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.javamaster.project2.Entity.User st = userRepo.findByUsername(username);

		if (st == null) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
			throw new UsernameNotFoundException("not found");
		}

		List<SimpleGrantedAuthority> list = 
				new ArrayList<SimpleGrantedAuthority>();

		for (UserRole role : st.getRoles()) {
			list.add(new SimpleGrantedAuthority(role.getRole()));
		}

		// tao user cua security 
		// user dang nhap hien tai
		User currentUser = new User(
				username, st.getPassword(), list);

		return currentUser;
	}
}
