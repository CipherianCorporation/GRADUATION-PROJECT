package com.edu.graduationproject.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.edu.graduationproject.entity.Product;

public interface ProductService {
    List<Product> findAll();

    List<Product> findAll(Sort sort);

    Product findById(Integer id);

    List<Product> findByCategoryId(Integer cid);

    List<Product> findBySubCategoryId(Integer sid);

    List<Product> findAllBySaleOff();

    List<Product> findAllByPriceRange(Double low, Double high);

    List<Product> searchProducts(String query);

    Product create(Product product);

    Product update(Product product);

    void delete(Integer id);

}
