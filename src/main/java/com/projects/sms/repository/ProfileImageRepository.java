package com.projects.sms.repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projects.sms.entity.Post;
import com.projects.sms.entity.ProfileImage;

import jakarta.transaction.Transactional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
	@Query(value = "SELECT * FROM profile WHERE blogger_id = :blogger_id", nativeQuery = true)
    Optional <ProfileImage> findProfileImageByBloggerId(@Param("blogger_id") Long bloggerId);
	
	@Query(value="SELECT COUNT(*) > 0 FROM profile WHERE blogger_id=:blogger_id", nativeQuery = true)
	boolean existsByBloggerId(@Param("blogger_id") Long bloggerId);
	
	@Modifying
	@Transactional
	@Query(value="delete from profile where blogger_id=:blogger_id",nativeQuery = true)
	void deleteProfileImageById(@Param("blogger_id") Long bloggerId);

	@Query(value="SELECT COUNT(*) > 0 FROM profile WHERE image_name=:image_name", nativeQuery=true)
	boolean existsByFileName(@Param("image_name") String imageName);
	
}
