package com.edu.graduationproject.controller.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.edu.graduationproject.entity.SubCategory;
import com.edu.graduationproject.model.IProductSoldBySubCategoryCount;
import com.edu.graduationproject.service.SubCategoryService;

@CrossOrigin("*")
@RestController
public class SubCategoryRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryRestController.class);

    @Autowired
    SubCategoryService service;

    // return list of sub-categories
    @GetMapping("/rest/sub-categories")
    public ResponseEntity<List<SubCategory>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // return list of sub-categories by category_id
    @GetMapping("/rest/sub-categories/{category_id}")
    public ResponseEntity<List<SubCategory>> getSubCatesByCatesId(@PathVariable Integer category_id) {
        try {
            return ResponseEntity.ok(service.findAllByCatesId(category_id));
        } catch (Exception e) {
            LOGGER.error("Error when calling getSubCatesByCatesId", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rest/sub-categories/product-sold")
    public ResponseEntity<List<IProductSoldBySubCategoryCount>> getProductSold() {
        return ResponseEntity.ok(service.getProductSold());
    }
    
    @PostMapping("/rest/sub-categories")
    public ResponseEntity<SubCategory> create(@RequestBody SubCategory subcategory){
        if (subcategory.getIsDeleted() == null) {
            subcategory.setIsDeleted(false);
        }
    	return ResponseEntity.ok(service.save(subcategory));
    }
    
    @PutMapping("/rest/sub-categories/{id}")
    public ResponseEntity<SubCategory> update(@PathVariable("id") Integer id, @RequestBody SubCategory subcategory){
        if (subcategory.getIsDeleted() == null) {
            subcategory.setIsDeleted(false);
        }
    	return ResponseEntity.ok(service.update(subcategory));
    }
    
    @DeleteMapping("/rest/sub-categories/{id}")
    public void delete(@PathVariable("id") Optional<Integer> id) {
    	service.deleteById(id.get());
    }
}
