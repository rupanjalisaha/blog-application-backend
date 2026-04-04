package com.projects.sms.springboot.exception;

public class BloggerNotFoundException extends RuntimeException{
	public BloggerNotFoundException(Long id) {
		super("Could not find blogger with id "+id);
	}

}
