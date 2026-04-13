package com.projects.sms.controller;
import jakarta.transaction.Transactional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projects.sms.entity.Post;
import com.projects.sms.repository.PostRepository;
import com.projects.sms.service.PostService;
import com.projects.sms.springboot.exception.PostNotFoundAdvice;
import com.projects.sms.springboot.exception.PostNotFoundException;

@RestController // Marks this class as a REST controller
@RequestMapping("/UVB/blogs") // Base URL path for all endpoints in this class
@CrossOrigin("https://blogger-management-system.vercel.app")
//@CrossOrigin("http://localhost:3000")
public class PostController {
	private final PostNotFoundAdvice postNotFoundAdvice;
	
	@Autowired
	private PostRepository postRepository;
	
	PostController(PostNotFoundAdvice postNotFoundAdvice) { 
        this.postNotFoundAdvice = postNotFoundAdvice;
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
	
}
