package com.edu.graduationproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edu.graduationproject.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    @Query("SELECT u FROM Favorite u WHERE u.user.id = ?1")
    Optional<List<Favorite>> findFavoritesByUserId(Integer userId);

    @Query("SELECT u FROM Favorite u WHERE u.user.id = ?1 AND u.product.id = ?2")
    Optional<List<Favorite>> findFavoritesByUserIdAndProductId(Integer userId, Integer productId);
}
