package com.projects.sms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projects.sms.entity.Like;

public interface LikeRepository extends JpaRepository<Like,Long>{
	
    @Query("SELECT l FROM Like l WHERE l.user.Id = :userId AND l.post.Id = :postId")
	Optional<Like> findByUserIdAndPostId(@Param("userId") Long userId,
	                        @Param("postId") Long postId);

	@Query("SELECT COUNT(l) FROM Like l WHERE l.post.Id = :postId")
	long countByPostId(@Param("postId") Long postId);

}
