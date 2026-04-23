package com.projects.sms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.sms.entity.Like;

public interface LikeRepository extends JpaRepository<Like,Long>{
	
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    long countByPostId(Long postId);


}
