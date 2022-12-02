package com.edu.graduationproject.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.Favorite;
import com.edu.graduationproject.repository.FavoriteRepository;
import com.edu.graduationproject.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteRepository repo;

    @Override
    public Optional<List<Favorite>> findFavoritesByUserId(Integer userId) {
        return repo.findFavoritesByUserId(userId);
    }

    @Override
    public Optional<List<Favorite>> findFavoritesByUserIdAndProductId(Integer userId, Integer productId) {
        return repo.findFavoritesByUserIdAndProductId(userId, productId);
    }

    @Override
    public Optional<Favorite> findById(Integer id) {
        return repo.findById(id);
    }

    @Override
    public Favorite create(Favorite favorite) {
        return repo.save(favorite);
    }

    @Override
    public Favorite update(Favorite favorite) {
        return repo.save(favorite);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

}
