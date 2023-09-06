package com.sn.gestionstock.services.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.ArticleDao;
import com.sn.gestionstock.dao.ClientDao;
import com.sn.gestionstock.dao.CommandeClientDao;
import com.sn.gestionstock.dao.LigneCommandeClientDao;
import com.sn.gestionstock.dtos.ArticleDtos;
import com.sn.gestionstock.dtos.ClientDto;
import com.sn.gestionstock.dtos.CommandeClientDto;
import com.sn.gestionstock.dtos.LigneCommandeClientDto;
import com.sn.gestionstock.dtos.MvtStockDto;
import com.sn.gestionstock.model.Article;
import com.sn.gestionstock.model.Client;
import com.sn.gestionstock.model.CommandeClient;
import com.sn.gestionstock.model.Etatcommande;
import com.sn.gestionstock.model.LigneCommandeClient;
import com.sn.gestionstock.model.SourceMvtStck;
import com.sn.gestionstock.model.TypeMvtStk;
import com.sn.gestionstock.services.CommandeClientService;
import com.sn.gestionstock.services.MvtStockService;
import com.sn.gestionstock.validator.ArticleValidator;
import com.sn.gestionstock.validator.CommandeClientValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {
	
	private CommandeClientDao commandeClientDao;
	
	private LigneCommandeClientDao ligneCommandeClientDao;
	
	private ArticleDao articleDao;
	
	private ClientDao clientDao;
	
	private MvtStockService mvtStockService;
	
	

	@Autowired
	public CommandeClientServiceImpl(CommandeClientDao commandeClientDao, LigneCommandeClientDao ligneCommandeClientDao,
			ArticleDao articleDao, ClientDao clientDao, MvtStockService mvtStockService) {
		
		this.commandeClientDao = commandeClientDao;
		this.ligneCommandeClientDao = ligneCommandeClientDao;
		this.articleDao = articleDao;
		this.clientDao = clientDao;
		this.mvtStockService = mvtStockService;
	}

	@Override
	public CommandeClientDto save(CommandeClientDto dto) {
	    List<String> errors = CommandeClientValidator.validate(dto);

	    if (!errors.isEmpty()) {
	      log.error("Commande client n'est pas valide");
	      throw new InvalidEntityException("La commande client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
	    }

	    if (dto.getId() != null && dto.isCommandeLivree()) {
	      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
	    }

	    Optional<Client> client = clientDao.findById(dto.getClient().getId());
	    if (client.isEmpty()) {
	      log.warn("Client with ID {} was not found in the DB", dto.getClient().getId());
	      throw new EntityNotfoundException("Aucune category avec l'ID " + dto.getClient().getId() + " n' ete trouve dans la BDD",
		            ErrorCodes.CLIENT_NOT_FOUND)
	      ;
	    }

	    List<String> articleErrors = new ArrayList<>();

	    if (dto.getLigneCommandeClients() != null) {
	      dto.getLigneCommandeClients().forEach(ligCmdClt -> {
	        if (ligCmdClt.getArticle() != null) {
	          Optional<Article> article = articleDao.findById(ligCmdClt.getArticle().getId());
	          if (article.isEmpty()) {
	            articleErrors.add("L'article avec l'ID " + ligCmdClt.getArticle().getId() + " n'existe pas");
	          }
	        } else {
	          articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
	        }
	      });
	    }

	    if (!articleErrors.isEmpty()) {
	      log.warn("");
	      throw new InvalidEntityException("Article n'existe pas dans la BDD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
	    }
	    dto.setDateCommande(Instant.now());
	    CommandeClient savedCmdClt = commandeClientDao.save(CommandeClientDto.toEntity(dto));

	    if (dto.getLigneCommandeClients() != null) {
	      dto.getLigneCommandeClients().forEach(ligCmdClt -> {
	        LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdClt);
	        ligneCommandeClient.setCommandeClient(savedCmdClt);
	        ligneCommandeClient.setIdEntreprise(dto.getIdEntreprise());
	        LigneCommandeClient savedLigneCmd = ligneCommandeClientDao.save(ligneCommandeClient);

	        effectuerSortie(savedLigneCmd);
	      });
	    }

	    return CommandeClientDto.fromEntity(savedCmdClt);
	}

	
	//modifier etat commande
	@Override
	public CommandeClientDto updateEtatCommande(Integer idCommande, Etatcommande etatCommande) {
		checkIdCommande(idCommande);
	    if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
	      log.error("L'etat de la commande client is NULL");
	      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
	          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
	    }
	    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
	    commandeClient.setEtatCommande(etatCommande);

	    CommandeClient savedCmdClt = commandeClientDao.save(CommandeClientDto.toEntity(commandeClient));
	    if (commandeClient.isCommandeLivree()) {
	      updateMvtStk(idCommande);
	    }

	    return CommandeClientDto.fromEntity(savedCmdClt);
	}
	
	//modifier quantiter commande
	@Override
	public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
		   checkIdCommande(idCommande);
		    checkIdLigneCommande(idLigneCommande);

		    if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
		          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
		    }

		    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
		    Optional<LigneCommandeClient> ligneCommandeClientOptional = findLigneCommandeClient(idLigneCommande);

		    LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
		    ligneCommandeClient.setQuantite(quantite);
		    ligneCommandeClientDao.save(ligneCommandeClient);

		    return commandeClient;
	}

	//modifier commande client
	@Override
	public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {
		checkIdCommande(idCommande);
	    if (idClient == null) {
	      log.error("L'ID du client is NULL");
	      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null",
	          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
	    }
	    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
	    Optional<Client> clientOptional = clientDao.findById(idClient);
	    if (clientOptional.isEmpty()) {
	      throw new EntityNotfoundException("Aucun client n'a ete trouve avec l'ID" + idClient + " n' ete trouve dans la BDD",
		            ErrorCodes.CLIENT_NOT_FOUND);
	    }
	    commandeClient.setClient(ClientDto.fromEntity(clientOptional.get()));

	    return CommandeClientDto.fromEntity(
	        commandeClientDao.save(CommandeClientDto.toEntity(commandeClient))
	    );
	}

	
	//modifier commade article
	@Override
	public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
		checkIdCommande(idCommande);
	    checkIdLigneCommande(idLigneCommande);
	    checkIdArticle(idArticle, "nouvel");

	    CommandeClientDto commandeClient = checkEtatCommande(idCommande);

	    Optional<LigneCommandeClient> ligneCommandeClient = findLigneCommandeClient(idLigneCommande);

	    Optional<Article> articleOptional = articleDao.findById(idArticle);
	    if (articleOptional.isEmpty()) {
	      throw new EntityNotfoundException("Aucune article n'a ete trouve avec l'ID" + idArticle + " n' ete trouve dans la BDD",
		            ErrorCodes.ARTICLE_NOT_FOUND);
	    }

	    List<String> errors = ArticleValidator.validate(ArticleDtos.fromEntity(articleOptional.get()));
	    if (!errors.isEmpty()) {
	      throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
	    }

	    LigneCommandeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
	    ligneCommandeClientToSaved.setArticle(articleOptional.get());
	    ligneCommandeClientDao.save(ligneCommandeClientToSaved);

	    return commandeClient;
	}

	//supprimer article
	@Override
	public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
	    checkIdCommande(idCommande);
	    checkIdLigneCommande(idLigneCommande);

	    CommandeClientDto commandeClient = checkEtatCommande(idCommande);
	    // Just to check the LigneCommandeClient and inform the client in case it is absent
	    findLigneCommandeClient(idLigneCommande);
	    ligneCommandeClientDao.deleteById(idLigneCommande);

	    return commandeClient;
	}

	//find commande client by id
	@Override
	public CommandeClientDto findById(Integer id) {
		   if (id == null) {
			      log.error("Commande client ID is NULL");
			      return null;
			    }
			    return commandeClientDao.findById(id)
			        .map(CommandeClientDto::fromEntity)
			        .orElseThrow(() -> new  EntityNotfoundException("Aucune commande client n'a ete trouve avec l'ID " + id + " n' ete trouve dans la BDD",
				            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND));
	}
	
	
	//find commande client by code
	@Override
	public CommandeClientDto findByCode(String code) {
		 if (!StringUtils.hasLength(code)) {
		      log.error("Commande client CODE is NULL");
		      return null;
		    }
		    return commandeClientDao.findCommandeClientByCode(code)
		        .map(CommandeClientDto::fromEntity)
		        .orElseThrow(() -> new  EntityNotfoundException("Aucune commande client n'a ete trouve avec le CODE " + code + " n' ete trouve dans la BDD",
			            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND));
	}

	//liste des commandes client
	@Override
	public List<CommandeClientDto> findAll() {
		
		return commandeClientDao.findAll().stream()
		        .map(CommandeClientDto::fromEntity)
		        .collect(Collectors.toList());
	}

	//ligne commande client par commande client id
	@Override
	public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
		
		return ligneCommandeClientDao.findAllByCommandeClientId(idCommande).stream()
		        .map(LigneCommandeClientDto::fromEntity)
		        .collect(Collectors.toList());
	}

	//supprimer commande client
	@Override
	public void delete(Integer id) {
		   if (id == null) {
			      log.error("Commande client ID is NULL");
			      return;
			    }
			    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientDao.findAllByCommandeClientId(id);
			    if (!ligneCommandeClients.isEmpty()) {
			      throw new InvalidOperationException("Impossible de supprimer une commande client deja utilisee",
			          ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
			    }
			    commandeClientDao.deleteById(id);
		
	}
	
	
	//effectuer sortie ligne
	  private void effectuerSortie(LigneCommandeClient lig) {
		    MvtStockDto mvtStkDto = MvtStockDto.builder()
		        .article(ArticleDtos.fromEntity(lig.getArticle()))
		        .dateMvt(Instant.now())
		        .typeMvt(TypeMvtStk.SORTIE)
		        .sourceMvt(SourceMvtStck.COMMANDE_CLIENT)
		        .quantite(lig.getQuantite())
		        .idEntreprise(lig.getIdEntreprise())
		        .build();
		    mvtStockService.sortieStock(mvtStkDto);
		  }
	  
	  //
	  private void checkIdCommande(Integer idCommande) {
		    if (idCommande == null) {
		      log.error("Commande client ID is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
		          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
		    }
		  }

		  private void checkIdLigneCommande(Integer idLigneCommande) {
		    if (idLigneCommande == null) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
		          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
		    }
		  }
		  
		  //
		  private void checkIdArticle(Integer idArticle, String msg) {
			    if (idArticle == null) {
			      log.error("L'ID de " + msg + " is NULL");
			      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
			          ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
			    }
			  }

			  private void updateMvtStk(Integer idCommande) {
			    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientDao.findAllByCommandeClientId(idCommande);
			    ligneCommandeClients.forEach(lig -> {
			      effectuerSortie(lig);
			    });
			  }
			  
			  
			  //
			  private CommandeClientDto checkEtatCommande(Integer idCommande) {
				    CommandeClientDto commandeClient = findById(idCommande);
				    if (commandeClient.isCommandeLivree()) {
				      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
				    }
				    return commandeClient;
				  }
			  
			 //
			  private Optional<LigneCommandeClient> findLigneCommandeClient(Integer idLigneCommande) {
				    Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientDao.findById(idLigneCommande);
				    if (ligneCommandeClientOptional.isEmpty()) {
				      throw new EntityNotfoundException("Aucune ligne commande client n'a ete trouve avec l'ID" + idLigneCommande + " n' ete trouve dans la BDD",
					            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND) ;
				    }
				    return ligneCommandeClientOptional;
				  }

}
