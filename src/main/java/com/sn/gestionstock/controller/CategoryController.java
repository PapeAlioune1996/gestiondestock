package com.sn.gestionstock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.CategoryDto;
import com.sn.gestionstock.services.CategoryService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class CategoryController {
	
	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		
		this.categoryService = categoryService;
	}
	
	//ajouter une category
	 @PostMapping(value =  "/categories/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CategoryDto save(@RequestBody CategoryDto categoryDto) {
		return categoryService.save(categoryDto);
	}
	 
	 //find category by id
	 @GetMapping(value =  "/categories/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
	 public CategoryDto findById(@PathVariable("idCategory") Integer idCategory) {
		 return categoryService.findById(idCategory);
	 }
	 
	 //find by code category
	 @GetMapping(value = "/categories/filter/{codeCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
	 public CategoryDto findByCodeCategory(@PathVariable("codeCategory") String codeCategory) {
		 return categoryService.findByCode(codeCategory);
		 
	 }
	 
	 //liste des category
	 @GetMapping(value= "/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<CategoryDto>  findAll(){
		 return categoryService.findAll();
	 }
	 
	 //supprimer category
	  @DeleteMapping(value =  "/categories/delete/{idCategory}")
	 public void deleteCategory(@PathVariable("idCategory") Integer idCategory) {
		 categoryService.delete(idCategory);
	 }
	
	

}
