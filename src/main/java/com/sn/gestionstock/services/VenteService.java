package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.VenteDto;

public interface VenteService {
	
	  VenteDto save(VenteDto dto);

	  VenteDto findById(Integer id);

	  VenteDto findByCode(String code);

	  List<VenteDto> findAll();

	  void delete(Integer id);

}
