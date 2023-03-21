package com.leonardo.springwebservice.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.leonardo.springwebservice.domain.Category;
import com.leonardo.springwebservice.domain.dto.CategoryDTO;
import com.leonardo.springwebservice.repositories.CategoryRepository;
import com.leonardo.springwebservice.services.exceptions.DataIntegrityException;
import com.leonardo.springwebservice.services.exceptions.ObjectNotFoundException;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return  categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Category.class.getName()));
    }

    public Category insert(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    public Category update(Category category) {
        Category newCategory = findById(category.getId());
        updateData(newCategory, category);
        return categoryRepository.save(newCategory);
    }

    private void updateData(Category newCategory, Category category) {
        newCategory.setName(category.getName());
    }

    public void deleteById(Integer id) {
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("A categoria possui produtos");
        }
    }

    public Page<Category> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return categoryRepository.findAll(pageRequest);
    }

    public Category fromDto(CategoryDTO categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }
}
