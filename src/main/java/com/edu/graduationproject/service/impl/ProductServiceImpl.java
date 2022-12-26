package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.Product;
import com.edu.graduationproject.repository.ProductRepository;
import com.edu.graduationproject.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository repo;

    @Override
    public List<Product> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Product> findAllIsDeletedFalse() {
        return repo.findAllIsDeletedFalse();
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return repo.findAll(sort);
    }

    @Override
    public List<Product> findAllBySaleOff() {
        return repo.findAllBySaleOff();
    }

    @Override
    public List<Product> findAllByPriceRange(Double min, Double max) {
        return repo.findAllByPriceRange(min, max);
    }

    @Override
    public Product findById(Integer id) {
        return repo.findById(id).get();
    }

    @Override
    public List<Product> findByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Product> findByCategoryId(Integer cid) {
        return repo.findByCategoryId(cid);
    }

    @Override
    public List<Product> findBySubCategoryId(Integer sid) {
        return repo.findBySubCategoryId(sid);
    }

    @Override
    public List<Product> searchProducts(String query) {
        return null;
    }

    @Override
    public Product create(Product product) {
        return repo.save(product);
    }

    @Override
    public Product update(Product product) {
        return repo.save(product);
    }

    @Override
    public Product delete(Integer id) {
        Optional<Product> findProduct = repo.findById(id);
        if (findProduct.isPresent()) {
            findProduct.get().setIsDeleted(true);
            findProduct.get().setDeletedAt(new Date());
            return repo.save(findProduct.get());
        }
        return null;
    }

}
