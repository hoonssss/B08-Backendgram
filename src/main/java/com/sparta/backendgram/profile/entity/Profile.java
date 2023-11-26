package com.sparta.backendgram.profile.entity;

import com.sparta.backendgram.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "profile_image")
    private String profileImage;

    private String introduction;

    public void setProfileImage(String imageName) {
        this.profileImage = imageName;
    }
}