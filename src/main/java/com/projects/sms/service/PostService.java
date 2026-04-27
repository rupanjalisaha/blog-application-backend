package com.projects.sms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.sms.entity.Post;
import com.projects.sms.entity.PostView;
import com.projects.sms.repository.PostRepository;
import com.projects.sms.repository.PostViewRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private PostViewRepository repo;

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findPostsByUsernameNative(username);
    }
    
    public void trackView(Long postId, Long userId, String ip) {
        boolean alreadyViewed = repo.existsByPostAndSession(postId, userId);
        
        try {
        if (!alreadyViewed) {
            PostView view = new PostView();
            view.setPostId(postId);
            view.setUserId(userId);
            view.setIpAddress(ip);
            view.setViewedAt(LocalDateTime.now());
            repo.save(view);
            postRepository.incrementViewCount(postId);
        }}catch(Exception e) {
        	System.out.println("logged some error "+e.getMessage());
        }
        }
}
