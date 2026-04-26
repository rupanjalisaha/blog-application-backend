package com.projects.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projects.sms.entity.Post;
import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
	
	@Query(value = "SELECT p.* FROM post p JOIN blogger b ON p.writer_name=b.username WHERE b.username = :username", nativeQuery = true)
    List<Post> findPostsByUsernameNative(@Param("username") String username);

	@Query(value="SELECT writer_name FROM post WHERE post.id = :postId", nativeQuery = true)
	String findUsernameBypostId(@Param("postId") Long postId);
	
	@Modifying
	@Transactional
	@Query(value="DELETE FROM post where writer_name = ?1", nativeQuery = true)
	void deletePostByWritername(String writerName);

	@Modifying 
	@Transactional 
	@Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId") 
	void incrementViewCount(Long postId);
}
