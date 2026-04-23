package com.projects.sms.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.projects.sms.entity.Like;
import com.projects.sms.repository.LikeRepository;
import com.projects.sms.repository.PostRepository;
import com.projects.sms.repository.UserRepository;

public class LikeService {
	
    @Autowired
    private LikeRepository likeRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;

    public String toggleLike(Long userId, Long postId) {

        Optional<Like> existing = likeRepo.findByUserIdAndPostId(userId, postId);

        if (existing.isPresent()) {
            likeRepo.delete(existing.get());
            return "Post unliked";
        }

        Like like = new Like();
        like.setUser(userRepo.findById(userId).orElseThrow());
        like.setPost(postRepo.findById(postId).orElseThrow());

        likeRepo.save(like);
        return "Post liked";
    }

    public long getLikeCount(Long postId) {
        return likeRepo.countByPostId(postId);
    }

}
