package com.projects.sms.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.BloggerDetails;
import com.projects.sms.entity.ProfileImage;
import com.projects.sms.repository.ProfileImageRepository;
import com.projects.sms.repository.RoleRepository;
import com.projects.sms.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;

@Service
public class CustomUserDetails implements UserDetailsService{

	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final ProfileImageRepository profileImageRepository;

	public CustomUserDetails(UserRepository userRepository, ProfileImageRepository profileImageRepository) {
		this.userRepository = userRepository;
		this.profileImageRepository= profileImageRepository;
	}
	
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    	
    	Blogger blogger = userRepository.findByUsername(username)
    			.orElseThrow(()->new UsernameNotFoundException("Blogger not found with username "+username));
    	if (!blogger.isVerified()) {
    	    throw new DisabledException("Please verify your email first");
    	}
                return new BloggerDetails(blogger);
    }
    
    public ProfileImage uploadImage(String name, Long bloggerId, MultipartFile file) {
    	
    	Blogger blogger = userRepository.findById(bloggerId)
    			.orElseThrow(()->new RuntimeException("Blogger not found"));
    			
    			
    	ProfileImage image = new ProfileImage();
    	image.setBloggerName(name);
    	image.setFileName(file.getOriginalFilename());
    	image.setFileType(file.getContentType());
    	try {
			image.setImageData(file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	blogger.addImage(image);
    	profileImageRepository.save(image);
    	userRepository.save(blogger);
    	return image;
    }
    
    public ProfileImage getImage(Long id){
    	return profileImageRepository.findById(id)
    			.orElseThrow(()->new RuntimeException("profile image not found"));
    }
}
