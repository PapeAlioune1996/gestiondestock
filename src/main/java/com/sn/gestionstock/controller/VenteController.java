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

import com.sn.gestionstock.dtos.VenteDto;
import com.sn.gestionstock.services.VenteService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class VenteController {
	
	private VenteService venteService;
	
	@Autowired
	public VenteController(VenteService venteService) {

		this.venteService = venteService;
	}

	@PostMapping("/ventes/create")
  public  VenteDto save(@RequestBody VenteDto dto) {
		
		return venteService.save(dto);
		  
	  }

	  @GetMapping("/ventes/{idVente}")
	  public VenteDto findById(@PathVariable("idVente") Integer id) {
		  return venteService.findById(id);
		  
	  }

	  @GetMapping("/ventes/{codeVente}")
	  public VenteDto findByCode(@PathVariable("codeVente") String code) {
		  return venteService.findByCode(code);
		  
	  }

	  @GetMapping("/ventes/all")
	 public List<VenteDto> findAll() {
		  
		  return venteService.findAll();
		  
	  }

	  @DeleteMapping("/ventes/delete/{idVente}")
	  void delete(@PathVariable("idVente") Integer id) {
		  venteService.delete(id);
	  }

}
