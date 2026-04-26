package com.projects.sms.entity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDto {

	    private Long id;
	    private String content;
	    private String username;
	    private ZonedDateTime createdAt;
	    public ZonedDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(ZonedDateTime createdAt) {
			this.createdAt = createdAt;
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

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public List<CommentDto> getReplies() {
			return replies;
		}

		public void setReplies(List<CommentDto> replies) {
			this.replies = replies;
		}

		private List<CommentDto> replies = new ArrayList<>();

	    public CommentDto(Comment comment) {
	        this.id = comment.getId();
	        this.content = comment.getContent();
	        this.username = comment.getUser().getUsername();
	        this.createdAt = comment.getCreatedAt();
	    }

	    public void addReply(CommentDto reply) {
	        this.replies.add(reply);
	    }

}
