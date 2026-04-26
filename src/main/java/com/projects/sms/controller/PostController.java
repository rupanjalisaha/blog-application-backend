package com.projects.sms.controller;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.CommentDto;
import com.projects.sms.entity.Post;
import com.projects.sms.repository.PostRepository;
import com.projects.sms.repository.UserRepository;
import com.projects.sms.service.CommentService;
import com.projects.sms.service.LikeService;
import com.projects.sms.springboot.exception.PostNotFoundAdvice;
import com.projects.sms.springboot.exception.PostNotFoundException;

@RestController // Marks this class as a REST controller
@RequestMapping("/UVB/blogs") // Base URL path for all endpoints in this class
@CrossOrigin("https://blogger-management-system.vercel.app")
//@CrossOrigin("http://localhost:3000")
public class PostController {
	
	private final PostNotFoundAdvice postNotFoundAdvice;
	
    private final LikeService likeService;
    
    private final CommentService commentService;

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	PostController(PostNotFoundAdvice postNotFoundAdvice, LikeService likeService, CommentService commentService) { 
        this.postNotFoundAdvice = postNotFoundAdvice;
        this.likeService = likeService;
        this.commentService = commentService;
    }

	@PostMapping("/writeBlogs")
	Post addPost(@RequestBody Post newPost){
    	Post post=new Post();
    	post.setWriterUsername(newPost.getWriterUsername());
    	post.setGenre(newPost.getGenre());
    	post.setPostTitle(newPost.getPostTitle());
    	post.setPostBody(newPost.getPostBody());
		return postRepository.save(post);
    }

	
	@GetMapping("/blogsDetails")
	List<Post> getBlogsDetails(){
		return postRepository.findAll();
	}
	
	@GetMapping("/blogsDetails/{id}")
	Post getPostById(@PathVariable Long id) {
		return postRepository.findById(id).orElseThrow(()->new PostNotFoundException(id));
	}
	
    @PostMapping("/blogsDetails/{postId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Object principal = auth.getPrincipal();
    	String username;
    	if (principal instanceof UserDetails) {
    	    username = ((UserDetails) principal).getUsername();
    	}else {
    		username = principal.toString();
    	}
    	Blogger blogger = userRepository.findByUsername(username).orElseThrow();
    	Long userId = blogger.getId();
        return ResponseEntity.ok(likeService.toggleLike(userId, postId));
    }

    @GetMapping("/blogsDetails/{postId}/count")
    public long getLikes(@PathVariable Long postId) {
        return likeService.getLikeCount(postId);
    }

	@Transactional
    @GetMapping("/blogsByUser/{username}")
    public List<Post> getPostsByUsername(@PathVariable String username) {
        return postRepository.findPostsByUsernameNative(username);
    }

	@PutMapping("/{id}")
	Post updatePost(@RequestBody Post newPost, @PathVariable Long id) {
		return postRepository.findById(id).map(post->{
			post.setWriterUsername(newPost.getWriterUsername());
			post.setGenre(newPost.getGenre());
			post.setPostTitle(newPost.getPostTitle());
			post.setPostBody(newPost.getPostBody());
			return postRepository.save(post);
		}).orElseThrow(()->new PostNotFoundException(id));
	}
	
	@DeleteMapping("/{id}")
	String deletePostMessage(@PathVariable Long id) {
		if(!postRepository.existsById(id)) {
			throw new PostNotFoundException(id);
		}
		postRepository.deleteById(id);
		return "Success: Post details with id "+id+" has been deleted";
	}
    
    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestParam Long postId,
                                        @RequestBody String content,
                                        @RequestParam(required = false) Long parentId) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Object principal = auth.getPrincipal();
    	String username;
    	if (principal instanceof UserDetails) {
    	    username = ((UserDetails) principal).getUsername();
    	}else {
    		username = principal.toString();
    	}
    	Blogger blogger = userRepository.findByUsername(username).orElseThrow();
    	Long userId = blogger.getId();

        return ResponseEntity.ok(
                commentService.addComment(userId, postId, content, parentId)
        );
    }

    @GetMapping("/comments/{postId}")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getNestedComments(postId);
    }

}
