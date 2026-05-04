package com.projects.sms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projects.sms.entity.Post;
import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>,JpaSpecificationExecutor<Post>{
	
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
	@Query(value="UPDATE post SET view_count = view_count + 1 WHERE id = :postId", nativeQuery=true) 
	void incrementViewCount(@Param("postId") Long postId);
	
	@Query(value="SELECT p.id,\r\n"
			+ "       p.article_title,\r\n"
			+ "       p.view_count,p.article_genre,\r\n"
			+ "	   p.article_body,\r\n"
			+ "	   p.writer_name,\r\n"
			+ "	   p.created_at,\r\n"
			+ "       COUNT(DISTINCT l.id) as likeCount,\r\n"
			+ "       COUNT(DISTINCT c.id) as commentCount,\r\n"
			+ "       (\r\n"
			+ "         (p.view_count * 1) +\r\n"
			+ "         (COUNT(DISTINCT l.id) * 4) +\r\n"
			+ "         (COUNT(DISTINCT c.id) * 6)\r\n"
			+ "       ) as score\r\n"
			+ "FROM post p\r\n"
			+ "LEFT JOIN post_likes l ON l.post_id = p.id\r\n"
			+ "LEFT JOIN Comment c ON c.post_id = p.id\r\n"
			+ "GROUP BY p.id\r\n"
			+ "ORDER BY score DESC LIMIT 5", nativeQuery=true)
	List<Object[]> findTopPosts();
	
	@Query(value="UPDATE post SET likes=likes+1 WHERE id=:postId", nativeQuery=true)
	public void increaseLikeCount(@Param("postId") Long postId);
	
	@Query(value="UPDATE post SET likes=likes-1 WHERE id=:postId", nativeQuery=true)
	public void reduceLikeCount(@Param("postId") Long postId);
}
