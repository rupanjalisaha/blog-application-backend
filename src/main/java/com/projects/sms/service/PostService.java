package com.projects.sms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.projects.sms.dtos.SearchRequest;
import com.projects.sms.entity.Post;
import com.projects.sms.entity.PostView;
import com.projects.sms.repository.PostRepository;
import com.projects.sms.repository.PostViewRepository;
import com.projects.sms.specification.*;
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
    public Page<Post> searchFreeTier(SearchRequest req) {

        // 🚫 Ignore premium filters explicitly
        req.setMinViews(null);
        req.setMaxViews(null);
        req.setAuthorId(null);
        req.setStartDate(null);
        req.setEndDate(null);

        Sort sort;

        String sortBy = req.getSortBy();

        if ("likes".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "likes");
        } else {
            sort = Sort.by(Sort.Direction.DESC, "createdAt");
        }
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);

        return postRepository.findAll(
                PostSpecification.filter(req),
                pageable
        );
    }

}
