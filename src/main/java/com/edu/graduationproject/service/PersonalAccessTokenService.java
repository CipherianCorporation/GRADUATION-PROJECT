package com.edu.graduationproject.service;

import java.util.Optional;

import com.edu.graduationproject.entity.PersonalAccessToken;

public interface PersonalAccessTokenService {
    Optional<PersonalAccessToken> findByToken(String token);

    PersonalAccessToken create(PersonalAccessToken pAccessToken);

    void deleteById(Integer id);
}
