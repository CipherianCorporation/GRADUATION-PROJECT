package com.edu.graduationproject.service;

import java.util.List;
import java.util.Optional;

import com.edu.graduationproject.entity.Favorite;

public interface FavoriteService {
    Optional<List<Favorite>> findFavoritesByUserId(Integer userId);
    
    Optional<List<Favorite>> findFavoritesByUserIdAndProductId(Integer userId, Integer productId);

    Optional<Favorite> findById(Integer id);
    
    Favorite create(Favorite favorite);

    Favorite update(Favorite favorite);

    void delete(Integer id);


}
