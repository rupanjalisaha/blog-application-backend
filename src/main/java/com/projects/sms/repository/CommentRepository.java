package com.projects.sms.repository;

import java.util.List;
import com.projects.sms.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Query("SELECT c FROM Comment c WHERE c.post.Id = :postId")
	List<Comment> findComments(@Param("postId") Long postId);
}
