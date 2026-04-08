package com.projects.sms.controller;

import com.projects.sms.entity.Blogger;
import com.projects.sms.entity.BloggerRequest;
import com.projects.sms.entity.Post;
import com.projects.sms.entity.ProfileImage;
import com.projects.sms.entity.ResetPasswordRequest;
import com.projects.sms.entity.Role;
import com.projects.sms.repository.ProfileImageRepository;
import com.projects.sms.repository.RoleRepository;
import com.projects.sms.repository.UserRepository;
import com.projects.sms.service.AuthService;
import com.projects.sms.service.BloggerRoleService;
import com.projects.sms.service.CustomUserDetails;
import com.projects.sms.service.ResetPasswordService;
import com.projects.sms.springboot.exception.BloggerNotFoundAdvice;
import com.projects.sms.springboot.exception.BloggerNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin("https://blogger-management-system.vercel.app")
@RestController
@RequestMapping("/UVB")
public class UserController {
    
	private final UserRepository userRepository;
	
	private final CustomUserDetails userDetails;
	
	private final BloggerRoleService bloggerRoleService;
	
    private final AuthService service;
	

	private final ResetPasswordService resetService;
	

	private final RoleRepository roleRepository;
	
	private final ProfileImageRepository profileImageRepository;
	
    UserController(BloggerRoleService bloggerRoleService,ResetPasswordService resetService, ProfileImageRepository profileImageRepository, UserRepository userRepository,AuthService service, RoleRepository roleRepository, CustomUserDetails userDetails) { 
        this.bloggerRoleService=bloggerRoleService;
        this.resetService=resetService;
        this.profileImageRepository=profileImageRepository;
        this.userRepository=userRepository;
        this.service=service;
        this.roleRepository = roleRepository;
        this.userDetails = userDetails;
    }
    public Set<Role> mapRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        return new HashSet<>(Collections.singleton(role));
    }
	
    @GetMapping("/")
    public String shareApiInfo(){
    	return ("blog portal apis are running now please use postman to test the apis");
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody BloggerRequest request) {
    	try {
        Blogger blogger = service.register(request);
        return ResponseEntity.ok(Map.of(
                "message", "Registered",
                "bloggerId", blogger.getId(),
                "username",blogger.getUsername()
        ));
    	}catch(Exception e) {
    		e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed"));
        }
    }
    @PostMapping("/admin/assign-role")
    public ResponseEntity<?> assignRole(@RequestParam String username,
                                        @RequestParam String role) {
        bloggerRoleService.addRoleToBlogger(username, role);
        return ResponseEntity.ok("Role assigned");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Blogger request) {
    	
    	Blogger blogger=userRepository.findByUsername(request.getUsername())
    			.orElseThrow(()->new RuntimeException("User not found"));
        try {
        String token = service.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(Map.of(
                "message", "Login Successful",
                "bloggerId", blogger.getId(),
                "username",blogger.getUsername(),
                "token", token
        ));
        }catch(Exception e) {
        	e.printStackTrace(); 
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed"));
        }
    }    
    
    @GetMapping("/bloggerDetails")
	List<Blogger> getBloggerDetails(){
		return userRepository.findAll();
	}
	
	@GetMapping("/user/bloggerDetails/{id}")
	public ResponseEntity<?> getBloggerById(@PathVariable Long id) {
		try {
		Optional<Blogger> targetUser = userRepository.findById(id);
		return ResponseEntity.ok().body(targetUser);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
		}
		
	}
	
	@PutMapping("/user/{id}")
	Blogger updateBlogger(@RequestBody BloggerRequest request, @PathVariable Long id) {
		Blogger targetUser = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    String loggedInUser = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

		boolean isAdmin = loggedInUser.equals("admin");
		 if (!loggedInUser.equals(targetUser.getUsername()) &&
		 !isAdmin) { throw new
		 RuntimeException("Unauthorized: Cannot edit other users"); }
		 
		return userRepository.findById(id).map(blogger->{
			blogger.setUsername(request.getUsername());
			blogger.setEmail(request.getEmail());
			blogger.setFullName(request.getFullName());
			blogger.setCategory(request.getCategory());
			blogger.setMessage(request.getMessage());
			blogger.setRoles((service.mapRole(request.getRole())));
			return userRepository.save(blogger);
		}).orElseThrow(()->new BloggerNotFoundException(id));
	}
	
	@PutMapping(value="/users/reset-password/{username}",consumes = "application/json")
	public ResponseEntity<?> resetPassword(
	        @RequestBody ResetPasswordRequest request,@PathVariable String username) {

		resetService.resetPassword(username, request.getNewPassword());
	    return ResponseEntity.ok("Password updated successfully");
	}
	
	@DeleteMapping("/{id}")
	String deleteBloggerMessage(@PathVariable Long id) {
	    Blogger targetUser = userRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    String loggedInUser = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

		
		if (!loggedInUser.equals(targetUser.getUsername())&&
		!loggedInUser.equals("admin")) { throw new
		RuntimeException("Unauthorized: Cannot delete other users"); }
		
		userRepository.deleteById(id);
		return "Success: Blogger details with id "+id+" has been deleted";
	}
	
	@PostMapping(value="/bloggers/images/{bloggerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable Long bloggerId,
            @RequestParam("file") MultipartFile file) {
        String loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Blogger blogger = userRepository.findById(bloggerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!loggedInUser.equals(blogger.getUsername()) && !loggedInUser.equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Unauthorized user"));
        }
        
        boolean profileExists = profileImageRepository.existsByBloggerId(bloggerId);
        boolean imageExists = profileImageRepository.existsByFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        if(profileExists && imageExists) {
        	profileImageRepository.deleteProfileImageByBloggerId(bloggerId);
            ProfileImage image = userDetails.uploadImage(blogger.getFullName(), bloggerId, file);
            return ResponseEntity.ok(Map.of(
                    "message", "Uploaded Profile Image",
                    "imageId", image.getId()
            ));
        	
        }else if(!profileExists && imageExists) {
        	return ResponseEntity.status(HttpStatus.FORBIDDEN)
        			.body(Map.of("error", "Profile Image"+StringUtils.cleanPath(file.getOriginalFilename())+" already exists for another user"));
        }
        else {
        ProfileImage image = userDetails.uploadImage(blogger.getFullName(), bloggerId, file);

        return ResponseEntity.ok(Map.of(
                "message", "Uploaded",
                "imageId", image.getId()
        ));
        }
    }

	@GetMapping(value="/bloggers/profileImages/{id}")
	
	public ResponseEntity<?> getProfileImageById(@PathVariable Long id) {
		boolean profileExists = profileImageRepository.existsByBloggerId(id);
		if(!profileExists) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No profile image exists for bloggerId"+id));
        }
		try {
        ProfileImage image = ( profileImageRepository.findProfileImageByBloggerId(id))
                .orElseThrow(() -> new RuntimeException("Image not found"));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())).body(image.getImageData());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Profile image for user id "+id+" was not found"));
		}
    }
	
	@DeleteMapping("/bloggers/deleteProfileImage/{id}")
	public ResponseEntity<?> deleteProfileImage(@PathVariable Long id) {
		
		ProfileImage image = ( profileImageRepository.findProfileImageByBloggerId(id))
                .orElseThrow(() -> new RuntimeException("Image not found"));
		 
		boolean imageExists = profileImageRepository.existsByBloggerId(id);
		
		
		if(imageExists) {
			profileImageRepository.deleteProfileImageByBloggerId(id);
		}
		return ResponseEntity.ok(Map.of("message","Profile image deleted successfully"));
	}
}

