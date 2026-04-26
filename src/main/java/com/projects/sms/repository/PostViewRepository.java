package com.projects.sms.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projects.sms.entity.PostView;

@Repository
public interface PostViewRepository extends JpaRepository<PostView,Long>{
	
	@Query("SELECT COUNT(p) > 0 FROM post_views p WHERE p.postId = :postId AND p.sessionId = :sessionId")
	boolean existsByPostAndSession(Long postId, String sessionId);

}
