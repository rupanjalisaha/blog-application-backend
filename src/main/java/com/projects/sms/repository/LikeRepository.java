package com.projects.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projects.sms.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long>{
	
    @Query(value="SELECT * FROM post_likes WHERE user_id = :userId AND post_id = :postId", nativeQuery=true)
	Optional<Like> findByUserIdAndPostId(@Param("userId") Long userId,
	                        @Param("postId") Long postId);

	@Query(value="SELECT COUNT(*) FROM post_likes WHERE post_id = :postId", nativeQuery=true)
	long countByPostId(@Param("postId") Long postId);

	@Query(value="SELECT post_id FROM post_likes GROUP BY post_id ORDER BY COUNT(id) DESC LIMIT 5", nativeQuery=true)
	List<Long> findPostIdofTopPosts();
}
