package com.projects.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projects.sms.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>{
	
	@Query(value = "SELECT p.* FROM post p JOIN blogger b ON p.writer_name=b.username WHERE b.username = :username", nativeQuery = true)
    List<Post> findPostsByUsernameNative(@Param("username") String username);

}
