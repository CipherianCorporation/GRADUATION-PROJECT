package com.edu.graduationproject.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.graduationproject.entity.Category;
import com.edu.graduationproject.repository.CategoryRepository;
import com.edu.graduationproject.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository repo;

	@Override
	public List<Category> findAll() {
		return repo.findAll();
	}

	@Override
	public Category save(Category category) {
		return repo.save(category);
	}

	@Override
	public Category update(Category category) {
		return repo.save(category);
	}

	@Override
	public void deleteById(Integer id) {
		// repo.deleteById(id);
		Optional<Category> findCates = repo.findById(id);
		if (findCates.isPresent()) {
			findCates.get().setIsDeleted(true);
			findCates.get().setDeletedAt(new Date());
			repo.save(findCates.get());
		}
	}

	@Override
	public List<Category> findByCateName(String name) {
		return repo.findByName(name);
	}
}
