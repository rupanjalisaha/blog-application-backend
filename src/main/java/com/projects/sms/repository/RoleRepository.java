package com.projects.sms.repository;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	
    public Optional<Role> findByName(String roleName);

}
