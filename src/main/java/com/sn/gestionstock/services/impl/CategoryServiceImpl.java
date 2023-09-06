package com.sn.gestionstock.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.ArticleDao;
import com.sn.gestionstock.dao.CategoryDao;
import com.sn.gestionstock.dtos.CategoryDto;
import com.sn.gestionstock.model.Article;
import com.sn.gestionstock.services.CategoryService;
import com.sn.gestionstock.validator.CategoryValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
	
	private CategoryDao categoryDao;
	
	private ArticleDao articleDao;
	
	
	@Autowired
	public CategoryServiceImpl(CategoryDao categoryDao, ArticleDao articleDao) {
	
		this.categoryDao = categoryDao;
		this.articleDao = articleDao;
	}

	
	//ajouter category
	@Override
	public CategoryDto save(CategoryDto dto) {
	    List<String> errors = CategoryValidator.validate(dto);
	    if (!errors.isEmpty()) {
	      log.error("Article is not valid {}", dto);
	      throw new InvalidEntityException("La category n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
	    }
	    return CategoryDto.fromEntity(
	    		categoryDao.save(CategoryDto.toEntity(dto))
	    );
	}

	
	//find category by id
	@Override
	public CategoryDto findById(Integer id) {
	    if (id == null) {
	        log.error("Category ID is null");
	        return null;
	      }
	      return categoryDao.findById(id)
	          .map(CategoryDto::fromEntity)
	          .orElseThrow(() -> new EntityNotfoundException("Aucune category avec l'ID " + id + " n' ete trouve dans la BDD",
			            ErrorCodes.CATEGORY_NOT_FOUND)
	        		  
	          );
	}

	//category par id
	@Override
	public CategoryDto findByCode(String code) {
	    if (!StringUtils.hasLength(code)) {
	        log.error("Category CODE is null");
	        return null;
	      }
	      return categoryDao.findCategoryByCode(code)
	          .map(CategoryDto::fromEntity)
	          .orElseThrow(() -> new EntityNotfoundException("Aucune category avec le CODE =" + code + " n' ete trouve dans la BDD",
			            ErrorCodes.CATEGORY_NOT_FOUND)
	          );
	}

	//liste des categories
	@Override
	public List<CategoryDto> findAll() {
	    return categoryDao.findAll().stream()
	            .map(CategoryDto::fromEntity)
	            .collect(Collectors.toList());
	}

	//supprimer une category
	@Override
	public void delete(Integer id) {
		   if (id == null) {
			      log.error("Category ID is null");
			      return;
			    }
			    List<Article> articles = articleDao.findAllByCategoryId(id);
			    if (!articles.isEmpty()) {
			      throw new InvalidOperationException("Impossible de supprimer cette categorie qui est deja utilise",
			          ErrorCodes.CATEGORY_ALREADY_IN_USE);
			    }
			    categoryDao.deleteById(id);
			  }
		
	

}
