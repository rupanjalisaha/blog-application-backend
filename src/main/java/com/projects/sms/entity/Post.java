package com.projects.sms.entity;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="post")
public class Post {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long Id;
	
	@Column(name = "writer_name", nullable=false)
    private String writerUsername;
	
	@Column(name="article_genre")
	public String genre;
	
	@Column(name="article_title",nullable=false)
	public String postTitle;
	
	@Column(name="created_at")
	public LocalDateTime createdAt;
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Basic(fetch = FetchType.EAGER)
	@Column(name="article_body", columnDefinition = "TEXT", nullable=false)
	private String postBody;
	
	
	public Long getPostId() {
		return Id;
	}

	public void setPostId(Long Id) {
		this.Id = Id;
	}

	public String getWriterUsername() {
		return writerUsername;
	}

	public void setWriterUsername(String writerUsername) {
		this.writerUsername = writerUsername;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostBody() {
		return postBody;
	}

	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}
	

}
