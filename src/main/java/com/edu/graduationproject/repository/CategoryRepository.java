package com.edu.graduationproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.edu.graduationproject.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Modifying
    @Query(value = "SELECT u.name FROM categories u WHERE u.name=?1 AND u.is_deleted = 0", nativeQuery = true)
    public List<Category> findByName(String name);
	
}
