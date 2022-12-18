package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.SubCategory;
import com.edu.graduationproject.model.IProductSoldBySubCategoryCount;
import com.edu.graduationproject.repository.SubCategoryRepository;
import com.edu.graduationproject.service.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    SubCategoryRepository repo;

    @Override
    public List<SubCategory> findAll() {
        return repo.findAll();
    }

    @Override
    public List<SubCategory> findAllByCatesId(Integer category_id) {
        return repo.findAllByCatesId(category_id);
    }

    @Override
    public List<IProductSoldBySubCategoryCount> getProductSold() {
        return repo.getProductSold();
    }

    @Override
    public SubCategory save(SubCategory subcategory) {
        return repo.save(subcategory);
    }

    @Override
    public SubCategory update(SubCategory subcategory) {
        return repo.save(subcategory);
    }

    @Override
    public void deleteById(Integer id) {
        // repo.deleteById(id);
        Optional<SubCategory> findSubCates = repo.findById(id);
        if (findSubCates.isPresent()) {
			findSubCates.get().setIsDeleted(true);
			findSubCates.get().setDeletedAt(new Date());
			repo.save(findSubCates.get());
		}
    }

}
