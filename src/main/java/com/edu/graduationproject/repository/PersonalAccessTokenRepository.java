package com.edu.graduationproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edu.graduationproject.entity.PersonalAccessToken;

public interface PersonalAccessTokenRepository extends JpaRepository<PersonalAccessToken, Integer>{

    @Query("SELECT p FROM PersonalAccessToken p WHERE p.token = :token")
    Optional<PersonalAccessToken> findByToken(String token);
    
}
