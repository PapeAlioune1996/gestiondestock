package com.sn.gestionstock.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.CommandeFournisseurDto;
import com.sn.gestionstock.dtos.LigneCommandeFournisseurDto;
import com.sn.gestionstock.model.Etatcommande;
import com.sn.gestionstock.services.CommandeFournisseurService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class CommandeFournisseurController {
	
	private CommandeFournisseurService commandeFournisseurService;

	@Autowired
	public CommandeFournisseurController(CommandeFournisseurService commandeFournisseurService) {
		
		this.commandeFournisseurService = commandeFournisseurService;
	}
	
	
	//

	  @PostMapping("/commandefournisseur/create")
	  public CommandeFournisseurDto save(@RequestBody CommandeFournisseurDto dto) {
		  return commandeFournisseurService.save(dto);
	  }
	  
	  //

	  @PatchMapping("/commandefournisseur/update/etat/{idCommande}/{etatCommande}")
	  public CommandeFournisseurDto updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") Etatcommande etatCommande) {
		  return commandeFournisseurService.updateEtatCommande(idCommande, etatCommande);
	  }

	  @PatchMapping("/commandefournisseur/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
	  public CommandeFournisseurDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
	      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite) {
		  return commandeFournisseurService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
	  }

	  @PatchMapping("/commandefournisseur/update/fournisseur/{idCommande}/{idFournisseur}")
	  public CommandeFournisseurDto updateFournisseur(@PathVariable("idCommande") Integer idCommande, @PathVariable("idFournisseur") Integer idFournisseur) {
		  return commandeFournisseurService.updateFournisseur(idCommande, idFournisseur);
	  }

	  @PatchMapping("/commandefournisseur/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
	  CommandeFournisseurDto updateArticle(@PathVariable("idCommande") Integer idCommande,
	      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle) {
		  return commandeFournisseurService.updateArticle(idCommande, idLigneCommande, idArticle);
	  }

	  @DeleteMapping("/commandefournisseur/delete/article/{idCommande}/{idLigneCommande}")
	  public CommandeFournisseurDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande) {
		  return commandeFournisseurService.deleteArticle(idCommande, idLigneCommande);
	  }

	  @GetMapping("/commandefournisseur/find/{idCommandeFournisseur}")
	 public CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id) {
		  return commandeFournisseurService.findById(id);
	  }

	  @GetMapping("/commandefournisseur/find/{codeCommandeFournisseur}")
	  CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code) {
		  return commandeFournisseurService.findByCode(code);
	  }

	  @GetMapping("/commandefournisseur/all")
	  List<CommandeFournisseurDto> findAll() {
		  return commandeFournisseurService.findAll();
	  }

	  @GetMapping("/commandefournisseur/lignesCommande/{idCommande}")
	  public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande) {
		  return commandeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande);
	  }

	  @DeleteMapping("/commandefournisseur/delete/{idCommandeFournisseur}")
	  void delete(@PathVariable("idCommandeFournisseur") Integer id) {
		  commandeFournisseurService.delete(id);
	  }
	

}
