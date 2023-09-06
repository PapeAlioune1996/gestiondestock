package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.ChangerMotDePasseUtilisateurDto;
import com.sn.gestionstock.dtos.UtilisateurDto;

public interface UtilisateurService {
	
	  UtilisateurDto save(UtilisateurDto dto);

	  UtilisateurDto findById(Integer id);

	  List<UtilisateurDto> findAll();

	  void delete(Integer id);

	  UtilisateurDto findByEmail(String email);

	  UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);

}
