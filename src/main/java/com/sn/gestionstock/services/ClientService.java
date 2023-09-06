package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.ClientDto;

public interface ClientService {
	
	  ClientDto save(ClientDto dto);

	  ClientDto findById(Integer id);

	  List<ClientDto> findAll();

	  void delete(Integer id);

}
