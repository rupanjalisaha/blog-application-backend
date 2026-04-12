package com.projects.sms.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class BloggerDetails implements UserDetails{

    private final Blogger blogger;

    public BloggerDetails(Blogger blogger) {
        this.blogger = blogger;
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return blogger.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return blogger.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return blogger.getUsername();
	}
	
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
