package com.projects.sms.repository;

import java.util.List;
import com.projects.sms.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List <Comment> findByPostId(Long postId);
}
