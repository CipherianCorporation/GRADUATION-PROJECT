package com.edu.graduationproject.controller.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.graduationproject.entity.Favorite;
import com.edu.graduationproject.service.FavoriteService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@Slf4j
@RestController
public class FavoriteRestController {
    @Autowired
    FavoriteService service;

    @GetMapping("/rest/favorites/userId/{userId}")
    public ResponseEntity<List<Favorite>> getFavoritesByUserId(@PathVariable("userId") Integer userId)
            throws Exception {
        Optional<List<Favorite>> listFav = service.findFavoritesByUserId(userId);
        if (listFav.isPresent()) {
            return ResponseEntity.ok(listFav.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Example: GET /rest/favorites/checkExist?userId=100001&productId=100001
    // nếu sp yêu thích có tồn tại api trả về true, ko tồn tại trả false
    @GetMapping("/rest/favorites/checkExist")
    public ResponseEntity<Boolean> checkFavExist(@RequestParam("userId") Integer userId,
            @RequestParam("productId") Integer productId)
            throws Exception {
        Optional<List<Favorite>> listFav = service.findFavoritesByUserIdAndProductId(userId, productId);
        if (listFav.isPresent()) {
            if (!listFav.get().isEmpty()) {
                Favorite fav = listFav.get().get(0);
                if (fav.getUser().getId().equals(userId) &&
                        fav.getProduct().getId().equals(productId)) {
                    return ResponseEntity.ok(true);
                }
            }
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/rest/favorites")
    public ResponseEntity<Favorite> postFavorites(@RequestBody Favorite favorite) {
        return ResponseEntity.ok(service.create(favorite));
    }

    @PutMapping("/rest/favorites/{id}")
    public ResponseEntity<Favorite> putFavorites(@PathVariable("id") Integer id, @RequestBody Favorite favorite) {
        Optional<Favorite> existFav = service.findById(id);
        if (existFav.isPresent()) {
            return ResponseEntity.ok(service.update(favorite));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/rest/favorites/{id}")
    public ResponseEntity<Object> deleteFavorites(@PathVariable("id") Integer id) {
        Optional<Favorite> existFav = service.findById(id);
        if (existFav.isPresent()) {
            service.delete(existFav.get().getId());
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.notFound().build();
    }
}
