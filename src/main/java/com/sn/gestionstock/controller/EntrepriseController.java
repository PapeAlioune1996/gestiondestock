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

import com.sn.gestionstock.dtos.EntrepriseDto;
import com.sn.gestionstock.services.EntrepriseService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class EntrepriseController {
	
	private EntrepriseService entrepriseService;

	@Autowired
	public EntrepriseController(EntrepriseService entrepriseService) {
		
		this.entrepriseService = entrepriseService;
	}
	
	//enregistre une entreprise
	 @PostMapping("/entreprise/create")
	 public  EntrepriseDto save(@RequestBody EntrepriseDto dto) {
		 return entrepriseService.save(dto);
	 }

	  @GetMapping("/entreprise/{idEntreprise}")
	  public EntrepriseDto findById(@PathVariable("idEntreprise") Integer id) {
		  return entrepriseService.findById(id);
	  }
	  
	  //liste des entreprise
	  @GetMapping( "/entreprise/all")
	  public List<EntrepriseDto> findAll() {
		  return entrepriseService.findAll();
	  }

	  //supprimer une entreprise
	  @DeleteMapping("/entreprise/delete/{idEntreprise}")
	  public void delete(@PathVariable("idEntreprise") Integer id) {
		  entrepriseService.delete(id);
	  }
	
	

}
