package com.projects.sms.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.BloggerRequest;
import com.projects.sms.entity.JwtUtil;
import com.projects.sms.entity.ResetPasswordRequest;
import com.projects.sms.entity.Role;
import com.projects.sms.repository.RoleRepository;
import com.projects.sms.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
    private AuthenticationManager manager;

	@Autowired
	private CustomUserDetails customUserDetails;
	
	@Autowired
	private EmailService emailService;
	
	private ResetPasswordRequest resetPassword;
	
    @Autowired
    private JwtUtil jwt;

    @Autowired
    private UserRepository repo;
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AuthService(UserRepository repo,RoleRepository roleRepository,PasswordEncoder passwordEncoder) {
    	this.repo=repo;
    	this.roleRepository=roleRepository;
    	this.passwordEncoder=passwordEncoder;
    }

    public Set<Role> setUserRole(Blogger user) {
    	 Set<Role> roles = user.getRoles().stream()
                .map(r -> roleRepository.findByName(r.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r.getName())))
                .collect(Collectors.toSet());
    	 return roles;
    }
    
    public Set<Role> mapRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        return new HashSet<>(Collections.singleton(role));
    }

    public Blogger register(BloggerRequest request) {
    	Blogger blogger=new Blogger();
    	blogger.setUsername(request.getUsername());
    	blogger.setPassword(passwordEncoder.encode(request.getPassword()));
    	blogger.setFullName(request.getFullName());
    	blogger.setEmail(request.getEmail());
    	blogger.setMessage(request.getMessage());
    	blogger.setCategory(request.getCategory()); 
        blogger.setRoles(mapRole(request.getRole()));
        String token = UUID.randomUUID().toString();
    	blogger.setVerificationToken(token);
    	blogger.setVerified(false);
    	blogger.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        repo.save(blogger);
        emailService.sendVerificationEmail(blogger.getEmail(), token);
        return blogger;
    }
    public String verifyEmail(String token) {

        Blogger blogger = repo.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (blogger.isVerified()) {
            return "Already verified";
        }

        if (blogger.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }

        blogger.setVerified(true);
        blogger.setVerificationToken(null);
        blogger.setExpiryDate(null);

        repo.save(blogger);

        return "Email verified successfully";
    }
    
	/*
	 * public void updatePassword(String username, String newPassword) { Blogger
	 * blogger = repo.findByUsername(username) .orElseThrow(() -> new
	 * RuntimeException("User not found"));
	 * 
	 * resetPassword.setNewPassword(passwordEncoder.encode(newPassword)); //
	 * IMPORTANT repo.save(blogger); }
	 */
    public String login(String username, String password) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);
        
        return jwt.generate(userDetails);
    }
}