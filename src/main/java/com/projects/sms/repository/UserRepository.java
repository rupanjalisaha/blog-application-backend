package com.projects.sms.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.Post;

public interface UserRepository extends JpaRepository<Blogger, Long> {
    Optional<Blogger> findByUsername(String username);
    
    Optional<Blogger> findByVerificationToken(String token);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE blogger SET password = :password WHERE username = :username", nativeQuery = true)
    int resetPasswordByUsername(@Param("username") String username, @Param("password") String password);

}
