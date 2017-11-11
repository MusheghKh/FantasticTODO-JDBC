package com.example.jdbcdemo.service;

import com.example.jdbcdemo.model.Role;
import com.example.jdbcdemo.model.User;
import com.example.jdbcdemo.repository.RoleRepositoryImpl;
import com.example.jdbcdemo.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;

    @Autowired
    public UserDetailServiceImpl(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Role role = roleRepository.findOne(user.getRole_id());
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()) );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}
