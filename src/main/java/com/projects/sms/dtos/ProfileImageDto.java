package com.projects.sms.dtos;
import java.util.Base64;

import com.projects.sms.entity.ProfileImage;

public class ProfileImageDto {

	    private Long id;
	    private String fileName;
	    private String fileType;
	    private String imageData; // Base64
	    private Long bloggerId;
	    private String bloggerName;
	    
	    public Long getBloggerId() {
			return bloggerId;
		}

		public void setBloggerId(Long bloggerId) {
			this.bloggerId = bloggerId;
		}

		public String getBloggerName() {
			return bloggerName;
		}

		public void setBloggerName(String bloggerName) {
			this.bloggerName = bloggerName;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileType() {
			return fileType;
		}

		public void setFileType(String fileType) {
			this.fileType = fileType;
		}

		public String getImageData() {
			return imageData;
		}

		public void setImageData(String imageData) {
			this.imageData = imageData;
		}

	    public ProfileImageDto(Long id, String fileName, String fileType, String imageData) {
	        this.id = id;
	        this.fileName = fileName;
	        this.fileType = fileType;
	        this.imageData = imageData;
	    }
	    
}
