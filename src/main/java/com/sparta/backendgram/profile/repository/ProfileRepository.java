package com.sparta.backendgram.profile.repository;

import com.sparta.backendgram.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
}
