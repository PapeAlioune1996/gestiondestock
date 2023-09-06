package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.FournisseurDto;

public interface FournisseurService {
	
	  FournisseurDto save(FournisseurDto dto);

	  FournisseurDto findById(Integer id);

	  List<FournisseurDto> findAll();

	  void delete(Integer id);

}
