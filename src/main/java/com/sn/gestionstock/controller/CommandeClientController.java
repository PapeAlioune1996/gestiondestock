package com.sn.gestionstock.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.CommandeClientDto;
import com.sn.gestionstock.dtos.LigneCommandeClientDto;
import com.sn.gestionstock.model.Etatcommande;
import com.sn.gestionstock.services.CommandeClientService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class CommandeClientController {
	
	private CommandeClientService commandeClientService;
	
	
	@Autowired
	  public CommandeClientController(CommandeClientService commandeClientService) {
		
		this.commandeClientService = commandeClientService;
	}

	//enregistre une commande
	@PostMapping( "/commandesclients/create")
	  public ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto) {
		  return ResponseEntity.ok(commandeClientService.save(dto));
	  }

	//modifier etat commande client
	  @PatchMapping("/commandesclients/update/etat/{idCommande}/{etatCommande}")
	  public ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") Etatcommande etatCommande){
		  return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
	  }

	//modifier quantite commande client
	  @PatchMapping("/commandesclients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
	  public ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
	      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite){
		  
		  return ResponseEntity.ok(commandeClientService.updateQuantiteCommande(idCommande, idLigneCommande, quantite));
	  }

	//modifier commande client
	  @PatchMapping("/commandesclients/update/client/{idCommande}/{idClient}")
	  public ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient) {
		  return ResponseEntity.ok(commandeClientService.updateClient(idCommande, idClient));
	  }
	  
	  //modifier ligne commande article et commande client
	  @PatchMapping("/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
	  public ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Integer idCommande,
	      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle) {
		  return ResponseEntity.ok(commandeClientService.updateArticle(idCommande, idLigneCommande, idArticle));
	  }

	  //supprimer commande client
	  @DeleteMapping( "/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
	  public ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande) {
		  return  ResponseEntity.ok(commandeClientService.deleteArticle(idCommande, idLigneCommande));
	  }

	  //commade by idcommande
	  @GetMapping("/commandesclients/{idCommandeClient}")
	  public ResponseEntity<CommandeClientDto> findById(@PathVariable Integer idCommandeClient){
		  return ResponseEntity.ok(commandeClientService.findById(idCommandeClient));
	  }

	  //afficher commande client par code
	  @GetMapping("/commandesclients/filter/{codeCommandeClient}")
	  public ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code) {
		  return ResponseEntity.ok(commandeClientService.findByCode(code));
	  }

	  //afficher la liste des commandes
	  @GetMapping( "/commandesclients/all")
	  public ResponseEntity<List<CommandeClientDto>> findAll(){
		 return ResponseEntity.ok(commandeClientService.findAll());
	  }

	  //liste des lignes commande client par id
	  @GetMapping( "/commandesclients/lignesCommande/{idCommande}")
	 public  ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande) {
		  return ResponseEntity.ok(commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande));
	  }

	  //supprimer commande client
	  @DeleteMapping("/commandesclients/delete/{idCommandeClient}")
	  public ResponseEntity<Void> delete(@PathVariable("idCommandeClient") Integer id){
		  commandeClientService.delete(id);
		return  ResponseEntity.ok().build();
	  }
}
