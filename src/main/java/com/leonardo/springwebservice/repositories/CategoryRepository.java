package com.leonardo.springwebservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leonardo.springwebservice.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
}
