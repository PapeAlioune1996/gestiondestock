package com.sn.gestionstock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.FournisseurDto;
import com.sn.gestionstock.services.FournisseurService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class FournisseurController {
	
	private FournisseurService fournisseurService;

	@Autowired
	public FournisseurController(FournisseurService fournisseurService) {
		
		this.fournisseurService = fournisseurService;
	}
	
	//
	  @PostMapping(value= "/fournisseurs/create")
	  public FournisseurDto save(@RequestBody FournisseurDto dto) {
		  return fournisseurService.save(dto);
	  }

	  @GetMapping("/fournisseurs/{idFournisseur}")
	  public FournisseurDto findById(@PathVariable("idFournisseur") Integer id) {
		  return fournisseurService.findById(id);
	  }

	  @GetMapping("/fournisseurs/all")
	  public List<FournisseurDto> findAll() {
		  return fournisseurService.findAll();
	  }

	  //
	  @DeleteMapping("/fournisseurs/delete/{idFournisseur}")
	  public void delete(@PathVariable("idFournisseur") Integer id) {
		  fournisseurService.delete(id);
	  }
	
	

}
