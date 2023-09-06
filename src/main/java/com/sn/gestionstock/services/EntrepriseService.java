package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.EntrepriseDto;

public interface EntrepriseService {
	
	  EntrepriseDto save(EntrepriseDto dto);

	  EntrepriseDto findById(Integer id);

	  List<EntrepriseDto> findAll();

	  void delete(Integer id);

}
