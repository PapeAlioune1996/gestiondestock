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
import com.sn.gestionstock.dao.CommandeFournisseurDao;
import com.sn.gestionstock.dao.FournisseurDao;
import com.sn.gestionstock.dao.LigneCommandeFournisseurDao;
import com.sn.gestionstock.dtos.ArticleDtos;
import com.sn.gestionstock.dtos.CommandeFournisseurDto;
import com.sn.gestionstock.dtos.FournisseurDto;
import com.sn.gestionstock.dtos.LigneCommandeFournisseurDto;
import com.sn.gestionstock.dtos.MvtStockDto;
import com.sn.gestionstock.model.Article;
import com.sn.gestionstock.model.CommandeFournisseur;
import com.sn.gestionstock.model.Etatcommande;
import com.sn.gestionstock.model.Fournisseur;
import com.sn.gestionstock.model.LigneCommandeFournisseur;
import com.sn.gestionstock.model.SourceMvtStck;
import com.sn.gestionstock.model.TypeMvtStk;
import com.sn.gestionstock.services.CommandeFournisseurService;
import com.sn.gestionstock.services.MvtStockService;
import com.sn.gestionstock.validator.ArticleValidator;
import com.sn.gestionstock.validator.CommandeFournisseurValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService{
	
	private CommandeFournisseurDao commandeFournisseurDao;
	
	private FournisseurDao fournisseurDao;
	
	private LigneCommandeFournisseurDao ligneCommandeFournisseurDao;
	
	private ArticleDao articleDao;
	
	private MvtStockService mvtStockService;
	
	
	@Autowired
	public CommandeFournisseurServiceImpl(CommandeFournisseurDao commandeFournisseurDao, FournisseurDao fournisseurDao,
			LigneCommandeFournisseurDao ligneCommandeFournisseurDao, ArticleDao articleDao,
			MvtStockService mvtStockService) {
		super();
		this.commandeFournisseurDao = commandeFournisseurDao;
		this.fournisseurDao = fournisseurDao;
		this.ligneCommandeFournisseurDao = ligneCommandeFournisseurDao;
		this.articleDao = articleDao;
		this.mvtStockService = mvtStockService;
	}

	//enregistre une commande fournisseur
	@Override
	public CommandeFournisseurDto save(CommandeFournisseurDto dto) {

	    List<String> errors = CommandeFournisseurValidator.validate(dto);

	    if (!errors.isEmpty()) {
	      log.error("Commande fournisseur n'est pas valide");
	      throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
	    }

	    if (dto.getId() != null && dto.isCommandeLivree()) {
	      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
	    }

	    Optional<Fournisseur> fournisseur = fournisseurDao.findById(dto.getFournisseur().getId());
	    if (fournisseur.isEmpty()) {
	      log.warn("Fournisseur with ID {} was not found in the DB", dto.getFournisseur().getId());
	      throw new EntityNotfoundException("Aucun fournisseur avec l'ID" + dto.getFournisseur().getId() + " n' ete trouve dans la BDD",
		            ErrorCodes.FOURNISSEUR_NOT_FOUND) ;
	    }

	    List<String> articleErrors = new ArrayList<>();

	    if (dto.getLigneCommandeFournisseurs() != null) {
	      dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
	        if (ligCmdFrs.getArticle() != null) {
	          Optional<Article> article = articleDao.findById(ligCmdFrs.getArticle().getId());
	          if (article.isEmpty()) {
	            articleErrors.add("L'article avec l'ID " + ligCmdFrs.getArticle().getId() + " n'existe pas");
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
	    CommandeFournisseur savedCmdFrs = commandeFournisseurDao.save(CommandeFournisseurDto.toEntity(dto));

	    if (dto.getLigneCommandeFournisseurs() != null) {
	      dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
	        LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFrs);
	        ligneCommandeFournisseur.setCommandeFournisseur(savedCmdFrs);
	        ligneCommandeFournisseur.setIdEntreprise(savedCmdFrs.getIdEntreprise());
	        LigneCommandeFournisseur saveLigne = ligneCommandeFournisseurDao.save(ligneCommandeFournisseur);

	        effectuerEntree(saveLigne);
	      });
	    }

	    return CommandeFournisseurDto.fromEntity(savedCmdFrs);
	}

	@Override
	public CommandeFournisseurDto updateEtatCommande(Integer idCommande, Etatcommande etatCommande) {
		 checkIdCommande(idCommande);
		    if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
		      log.error("L'etat de la commande fournisseur is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
		          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
		    }
		    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
		    commandeFournisseur.setEtatCommande(etatCommande);

		    CommandeFournisseur savedCommande = commandeFournisseurDao.save(CommandeFournisseurDto.toEntity(commandeFournisseur));
		    if (commandeFournisseur.isCommandeLivree()) {
		      updateMvtStk(idCommande);
		    }
		    return CommandeFournisseurDto.fromEntity(savedCommande);
	}

	@Override
	public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande,
			BigDecimal quantite) {
		 checkIdCommande(idCommande);
		    checkIdLigneCommande(idLigneCommande);

		    if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
		          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
		    }

		    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
		    Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = findLigneCommandeFournisseur(idLigneCommande);

		    LigneCommandeFournisseur ligneCommandeFounisseur = ligneCommandeFournisseurOptional.get();
		    ligneCommandeFounisseur.setQuantite(quantite);
		    ligneCommandeFournisseurDao.save(ligneCommandeFounisseur);

		    return commandeFournisseur;
	}

	@Override
	public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
	    checkIdCommande(idCommande);
	    if (idFournisseur == null) {
	      log.error("L'ID du fournisseur is NULL");
	      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID fournisseur null",
	          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
	    }
	    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
	    Optional<Fournisseur> fournisseurOptional = fournisseurDao.findById(idFournisseur);
	    if (fournisseurOptional.isEmpty()) {
	      throw new EntityNotfoundException("Aucun fournisseur avec l'ID" + idFournisseur + " n' ete trouve dans la BDD",
		            ErrorCodes.FOURNISSEUR_NOT_FOUND) ;
	    }
	    commandeFournisseur.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));

	    return CommandeFournisseurDto.fromEntity(
	        commandeFournisseurDao.save(CommandeFournisseurDto.toEntity(commandeFournisseur))
	    );
	}

	@Override
	public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
	    checkIdCommande(idCommande);
	    checkIdLigneCommande(idLigneCommande);
	    checkIdArticle(idArticle, "nouvel");

	    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);

	    Optional<LigneCommandeFournisseur> ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);

	    Optional<Article> articleOptional = articleDao.findById(idArticle);
	    if (articleOptional.isEmpty()) {
	      throw new EntityNotfoundException("Aucun fournisseur avec l'ID" + idArticle + " n' ete trouve dans la BDD",
		            ErrorCodes.ARTICLE_NOT_FOUND) ;
	    }

	    List<String> errors = ArticleValidator.validate(ArticleDtos.fromEntity(articleOptional.get()));
	    if (!errors.isEmpty()) {
	      throw new InvalidEntityException("Article invalid", ErrorCodes.ARTICLE_NOT_VALID, errors);
	    }

	    LigneCommandeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
	    ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
	    ligneCommandeFournisseurDao.save(ligneCommandeFournisseurToSaved);

	    return commandeFournisseur;
	}

	@Override
	public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
	    checkIdCommande(idCommande);
	    checkIdLigneCommande(idLigneCommande);

	    CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
	    // Il suffit de vérifier la Ligne Commande Fournisseur et d'informer le fournisseur en cas d'absence
	    findLigneCommandeFournisseur(idLigneCommande);
	    ligneCommandeFournisseurDao.deleteById(idLigneCommande);

	    return commandeFournisseur;
	}

	
	//commande fournisseur par id
	@Override
	public CommandeFournisseurDto findById(Integer id) {
	    if (id == null) {
	        log.error("Commande fournisseur ID is NULL");
	        return null;
	      }
	      return commandeFournisseurDao.findById(id)
	          .map(CommandeFournisseurDto::fromEntity)
	          .orElseThrow(() -> new EntityNotfoundException("Aucune commande fournisseur n'a ete trouve avec l'ID" + id + " n' ete trouve dans la BDD",
			            ErrorCodes.COMMANDE_CLIENT_NOT_FOUND));
	}

	//find commande fournissuer by code
	@Override
	public CommandeFournisseurDto findByCode(String code) {
		if (!StringUtils.hasLength(code)) {
		      log.error("Commande fournisseur CODE is NULL");
		      return null;
		    }
		    return commandeFournisseurDao.findCommandeFournisseurByCode(code)
		        .map(CommandeFournisseurDto::fromEntity)
		        .orElseThrow(() -> new EntityNotfoundException("Aucune commande fournisseur n'a ete trouve avec le CODE " + code + " n' ete trouve dans la BDD",
			            ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));
	}

	@Override
	public List<CommandeFournisseurDto> findAll() {
		  return commandeFournisseurDao.findAll().stream()
			        .map(CommandeFournisseurDto::fromEntity)
			        .collect(Collectors.toList());
	}

	@Override
	public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(
			Integer idCommande) {
		return ligneCommandeFournisseurDao.findAllByCommandeFournisseurId(idCommande).stream()
		        .map(LigneCommandeFournisseurDto::fromEntity)
		        .collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
		  if (id == null) {
		      log.error("Commande fournisseur ID is NULL");
		      return;
		    }
		    List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurDao.findAllByCommandeFournisseurId(id);
		    if (!ligneCommandeFournisseurs.isEmpty()) {
		      throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilisee",
		          ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
		    }
		    commandeFournisseurDao.deleteById(id);
		
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////	
	//methodes personnaliser
	  private CommandeFournisseurDto checkEtatCommande(Integer idCommande) {
		    CommandeFournisseurDto commandeFournisseur = findById(idCommande);
		    if (commandeFournisseur.isCommandeLivree()) {
		      throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
		    }
		    return commandeFournisseur;
		  }

		  private Optional<LigneCommandeFournisseur> findLigneCommandeFournisseur(Integer idLigneCommande) {
		    Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurDao.findById(idLigneCommande);
		    if (ligneCommandeFournisseurOptional.isEmpty()) {
		      throw new EntityNotfoundException("Aucune ligne commande fournisseur n'a ete trouve avec l'ID" + idLigneCommande + " n' ete trouve dans la BDD",
			            ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND) ;
		    }
		    return ligneCommandeFournisseurOptional;
		  }

		  private void checkIdCommande(Integer idCommande) {
		    if (idCommande == null) {
		      log.error("Commande fournisseur ID is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
		          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
		    }
		  }

		  private void checkIdLigneCommande(Integer idLigneCommande) {
		    if (idLigneCommande == null) {
		      log.error("L'ID de la ligne commande is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
		          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
		    }
		  }

		  private void checkIdArticle(Integer idArticle, String msg) {
		    if (idArticle == null) {
		      log.error("L'ID de " + msg + " is NULL");
		      throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
		          ErrorCodes.COMMANDE_FOURNISSEUR_NON_MODIFIABLE);
		    }
		  }

		  private void updateMvtStk(Integer idCommande) {
		    List<LigneCommandeFournisseur> ligneCommandeFournisseur = ligneCommandeFournisseurDao.findAllByCommandeFournisseurId(idCommande);
		    ligneCommandeFournisseur.forEach(lig -> {
		      effectuerEntree(lig);
		    });
		  }

		  private void effectuerEntree(LigneCommandeFournisseur lig) {
		    MvtStockDto mvtStkDto = MvtStockDto.builder()
		        .article(ArticleDtos.fromEntity(lig.getArticle()))
		        .dateMvt(Instant.now())
		        .typeMvt(TypeMvtStk.ENTREE)
		        .sourceMvt(SourceMvtStck.COMMANDE_FOURNISSEUR)
		        .quantite(lig.getQuantite())
		        .idEntreprise(lig.getIdEntreprise())
		        .build();
		    mvtStockService.entreeStock(mvtStkDto);
		  }
	
	

}
