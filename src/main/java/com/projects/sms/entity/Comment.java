package com.projects.sms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import com.projects.sms.entity.Blogger;

@Entity
public class Comment {
	    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    private Blogger user;
	    
	    @ManyToOne
	    private Post post;
	    
	    private String content;

		private LocalDateTime createdAt;

	    @ManyToOne
	    @JoinColumn(name="parent_id")
	    private Comment parent; // null = root comment

	    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
	    private List<Comment> replies = new ArrayList<>();

	    public Blogger getUser() {
			return user;
		}

		public void setUser(Blogger user) {
			this.user = user;
		}

		public Post getPost() {
			return post;
		}

		public void setPost(Post post) {
			this.post = post;
		}

		public Comment getParent() {
			return parent;
		}

		public void setParent(Comment parent) {
			this.parent = parent;
		}

		public List<Comment> getReplies() {
			return replies;
		}

		public void setReplies(List<Comment> replies) {
			this.replies = replies;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

}
