package com.projects.sms.service;

import java.util.Base64;

import com.projects.sms.dtos.ProfileImageDto;
import com.projects.sms.entity.ProfileImage;

public class ProfileImageMapper {
	
	public ProfileImageDto mapToDto(ProfileImage image) {

        String base64 = null;

        if (image.getImageData() != null) {
            base64 = Base64.getEncoder().encodeToString(image.getImageData());
        }

        return new ProfileImageDto(
            image.getId(),
            image.getFileName(),
            image.getFileType(),
            base64
        );
    }


}
