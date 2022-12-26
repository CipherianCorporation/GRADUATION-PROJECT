package com.edu.graduationproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.edu.graduationproject.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.category.id=?1 AND p.isDeleted = 0")
    List<Product> findByCategoryId(Integer cid);

    @Query("SELECT p FROM Product p WHERE p.subCategory.id=?1 AND p.isDeleted = 0")
    List<Product> findBySubCategoryId(Integer sid);

    @Query("SELECT p FROM Product p WHERE p.sale_off != 0 AND p.isDeleted = 0")
    List<Product> findAllBySaleOff();

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2 AND p.isDeleted = 0")
    List<Product> findAllByPriceRange(Double min, Double max);

    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.isDeleted = 0")
    List<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = 0")
    List<Product> findAllIsDeletedFalse();

}
