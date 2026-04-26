package com.projects.sms.repository;

import java.util.List;
import com.projects.sms.entity.Comment;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Query("SELECT c FROM Comment c WHERE c.post.Id = :postId")
	List<Comment> findByPostId(@Param("postId") Long postId);
	
	@Modifying
	@Transactional
	@Query("delete from Comment where id=:postId")
	void deleteByPostId(@Param("postId") Long postId);
}
