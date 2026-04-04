package com.projects.sms.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.Role;
import com.projects.sms.repository.RoleRepository;
import com.projects.sms.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
public class BloggerRoleService {

    private final UserRepository bloggerRepository;
    private final RoleRepository roleRepository;

    public BloggerRoleService(UserRepository bloggerRepository, RoleRepository roleRepository) {
        this.bloggerRepository = bloggerRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void addRoleToBlogger(String username, String roleName) {

        Blogger blogger = bloggerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        blogger.getRoles().add(role);

        // No explicit save needed if blogger is managed
    }
}
