package com.edu.graduationproject.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.PersonalAccessToken;
import com.edu.graduationproject.repository.PersonalAccessTokenRepository;
import com.edu.graduationproject.service.PersonalAccessTokenService;

@Service
public class PersonalAccessTokenServiceImpl implements PersonalAccessTokenService {
    
    @Autowired
    PersonalAccessTokenRepository repo;

    @Override
    public Optional<PersonalAccessToken> findByToken(String token) {
        return repo.findByToken(token);
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public PersonalAccessToken create(PersonalAccessToken pAccessToken) {
        return repo.save(pAccessToken);
    }
    
}
