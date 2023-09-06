package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.CategoryDto;

public interface CategoryService {
	
	  CategoryDto save(CategoryDto dto);

	  CategoryDto findById(Integer id);

	  CategoryDto findByCode(String code);

	  List<CategoryDto> findAll();

	  void delete(Integer id);

}
