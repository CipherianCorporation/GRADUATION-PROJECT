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

import com.edu.graduationproject.entity.Category;
import com.edu.graduationproject.entity.SubCategory;
import com.edu.graduationproject.service.CategoryService;

@CrossOrigin("*")
@RestController
public class CategoryRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRestController.class);

    @Autowired
    CategoryService categoryService;

    @GetMapping("/rest/categories")
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("/rest/categories/{name}")
    public List<Category> getCateByName(@PathVariable String name) {
        return categoryService.findByCateName(name);
    }

    @PostMapping("/rest/categories")
    public ResponseEntity<Category> create(@RequestBody Category category) {
        if (category.getIsDeleted() == null) {
            category.setIsDeleted(false);
        }
        return ResponseEntity.ok(categoryService.save(category));
    }

    @PutMapping("/rest/categories/{id}")
    public ResponseEntity<Category> update(@PathVariable("id") Integer id, @RequestBody Category category) {
        if (category.getIsDeleted() == null) {
            category.setIsDeleted(false);
        }
        return ResponseEntity.ok(categoryService.update(category));
    }

    @DeleteMapping("/rest/categories/{id}")
    public void delete(@PathVariable("id") Optional<Integer> id) {
        categoryService.deleteById(id.get());
    }
}
