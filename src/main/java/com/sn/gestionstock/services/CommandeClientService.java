package com.sn.gestionstock.services;

import java.math.BigDecimal;
import java.util.List;

import com.sn.gestionstock.dtos.CommandeClientDto;
import com.sn.gestionstock.dtos.LigneCommandeClientDto;
import com.sn.gestionstock.model.Etatcommande;

public interface CommandeClientService {
	
	CommandeClientDto save(CommandeClientDto dto);

	  CommandeClientDto updateEtatCommande(Integer idCommande, Etatcommande etatCommande);

	  CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

	  CommandeClientDto updateClient(Integer idCommande, Integer idClient);

	  CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

	  // Delete article ==> delete LigneCommandeClient
	  CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);

	  CommandeClientDto findById(Integer id);

	  CommandeClientDto findByCode(String code);

	  List<CommandeClientDto> findAll();

	  List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);

	  void delete(Integer id);
}
