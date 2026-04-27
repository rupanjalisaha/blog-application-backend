package com.projects.sms.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projects.sms.entity.PostView;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.query.Param;

@Repository
public interface PostViewRepository extends JpaRepository<PostView,Long>{
	
	@Query("SELECT COUNT(p) > 0 FROM PostView p WHERE p.postId = :postId AND p.userId = :userId")
	boolean existsByPostAndSession(@Param("postId") Long postId, @Param("userId") Long userId);

}
