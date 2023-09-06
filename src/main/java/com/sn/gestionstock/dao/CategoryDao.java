package com.sn.gestionstock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

	 Optional<Category> findCategoryByCode(String code);
}
