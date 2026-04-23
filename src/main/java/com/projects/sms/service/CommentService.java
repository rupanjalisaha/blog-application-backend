package com.projects.sms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.projects.sms.entity.Comment;
import com.projects.sms.repository.CommentRepository;
import com.projects.sms.repository.PostRepository;
import com.projects.sms.repository.UserRepository;
import com.projects.sms.entity.CommentDto;

public class CommentService {
	
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    public Comment addComment(Long userId, Long postId, String content, Long parentId) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(userRepo.findById(userId).orElseThrow());
        comment.setPost(postRepo.findById(postId).orElseThrow());

        if (parentId != null) {
            Comment parent = commentRepo.findById(parentId).orElseThrow();
            comment.setParent(parent);
        }

        return commentRepo.save(comment);
    }

    // Build nested comment tree
    public List<CommentDto> getNestedComments(Long postId) {

        List<Comment> comments = commentRepo.findByPostId(postId);

        Map<Long, CommentDto> map = new HashMap<>();
        List<CommentDto> roots = new ArrayList<>();

        // create DTOs
        for (Comment c : comments) {
            map.put(c.getId(), new CommentDto(c));
        }

        // build tree
        for (Comment c : comments) {
            if (c.getParent() != null) {
                CommentDto parent = map.get(c.getParent().getId());
                parent.addReply(map.get(c.getId()));
            } else {
                roots.add(map.get(c.getId()));
            }
        }

        return roots;
    }
}
