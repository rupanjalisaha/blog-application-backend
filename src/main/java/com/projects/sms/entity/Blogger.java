package com.projects.sms.entity;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="blogger")
public class Blogger{
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="username",nullable=false,unique=true)
	private String username;
	@Column(name="password",nullable=false)
	private String password;
	@Column(name="name")
	private String fullName;
	@Column(name="email_id",nullable=false,unique=true)
	private String email;
	@Column(name="message")
	private String message;
	private boolean isVerified = false;
	private String verificationToken;
	private LocalDateTime expiryDate;
	private String subscription = "Free";
	
	@OneToMany(
		    mappedBy = "blogger",
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
		private Set<ProfileImage> images = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "blogger_roles",
    joinColumns = @JoinColumn(name = "blogger_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles= new HashSet<>();
    
	
	public Set<Role> getRoles() {
		return roles;
	}
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String category;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void addImage(ProfileImage image) {
	    images.add(image);
	    image.setBlogger(this);
	}
	
	public Set<ProfileImage> getImages() {
		return images;
	}
	public void setImages(Set<ProfileImage> images) {
		this.images = images;
	}
	public void removeImage(ProfileImage image) {
	    images.remove(image);
	    image.setBlogger(null);
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	public String getVerificationToken() {
		return verificationToken;
	}
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	public String getSubscription() {
		return subscription;
	}
	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}
	
}
