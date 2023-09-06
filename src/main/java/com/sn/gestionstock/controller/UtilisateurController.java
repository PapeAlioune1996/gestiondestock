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

import com.sn.gestionstock.dtos.ChangerMotDePasseUtilisateurDto;
import com.sn.gestionstock.dtos.UtilisateurDto;
import com.sn.gestionstock.services.UtilisateurService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class UtilisateurController {
	
	private UtilisateurService utilisateurService;

	@Autowired
	public UtilisateurController(UtilisateurService utilisateurService) {
		
		this.utilisateurService = utilisateurService;
	}
	

	//ajouter une utilisateur
	  @PostMapping("/utilisateurs/create")
	  public UtilisateurDto save(@RequestBody UtilisateurDto dto) {
		  return utilisateurService.save(dto);
	  }

	  //modifier mot de passe
	  @PostMapping("/utilisateur/update/password")
	  public UtilisateurDto changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto dto) {
		  return utilisateurService.changerMotDePasse(dto);
	  }

	  //lister un utilisateur par id
	  @GetMapping("/utilisateur/{idUtilisateur}")
	  UtilisateurDto findById(@PathVariable("idUtilisateur") Integer id) {
		  return utilisateurService.findById(id);
	  }

	  //lister un user par mail
	  @GetMapping("/find/{email}")
	  public UtilisateurDto findByEmail(@PathVariable("email") String email) {
		  return utilisateurService.findByEmail(email);
	  }
	  

	  //lister tous les utilisateur
	  @GetMapping("/utilisateur/all")
	  public List<UtilisateurDto> findAll() {
		  return utilisateurService.findAll();
	  }

	  //supprimer un user
	  @DeleteMapping("/utilisateur/delete/{idUtilisateur}")
	  public void delete(@PathVariable("idUtilisateur") Integer id) {
		  utilisateurService.delete(id);
	  }

	
	

}
