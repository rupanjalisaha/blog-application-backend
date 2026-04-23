package com.projects.sms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projects.sms.entity.Like;

public interface LikeRepository extends JpaRepository<Like,Long>{
	
    @Query(value="SELECT * FROM post_likes WHERE user_id = :userId AND post_id = :postId", nativeQuery=true)
	Optional<Like> findByUserIdAndPostId(@Param("userId") Long userId,
	                        @Param("postId") Long postId);

	@Query(value="SELECT COUNT(*) FROM post_likes WHERE post_id = :postId", nativeQuery=true)
	long countByPostId(@Param("postId") Long postId);

}
