package com.projects.sms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projects.sms.entity.Post;
import com.projects.sms.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getPostsByUsername(String username) {
        return postRepository.findPostsByUsernameNative(username);
    }
}
