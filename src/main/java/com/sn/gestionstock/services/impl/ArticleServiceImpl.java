package com.sn.gestionstock.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.ArticleDao;
import com.sn.gestionstock.dao.LigneCommandeClientDao;
import com.sn.gestionstock.dao.LigneCommandeFournisseurDao;
import com.sn.gestionstock.dao.LigneVentesDao;
import com.sn.gestionstock.dtos.ArticleDtos;
import com.sn.gestionstock.dtos.LigneCommandeClientDto;
import com.sn.gestionstock.dtos.LigneCommandeFournisseurDto;
import com.sn.gestionstock.dtos.LigneVenteDto;
import com.sn.gestionstock.model.LigneCommandeClient;
import com.sn.gestionstock.model.LigneCommandeFournisseur;
import com.sn.gestionstock.model.LigneVente;
import com.sn.gestionstock.services.ArticleService;
import com.sn.gestionstock.validator.ArticleValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService{
	
	private ArticleDao articleDao;
	
	private LigneVentesDao ligneVentesDao;
	
	private LigneCommandeClientDao ligneCommandeClientDao;
	
	private LigneCommandeFournisseurDao ligneCommandeFournisseurDao;
	
	
	@Autowired
	public ArticleServiceImpl(ArticleDao articleDao, LigneVentesDao ligneVentesDao,
			LigneCommandeClientDao ligneCommandeClientDao, LigneCommandeFournisseurDao ligneCommandeFournisseurDao) {
		super();
		this.articleDao = articleDao;
		this.ligneVentesDao = ligneVentesDao;
		this.ligneCommandeClientDao = ligneCommandeClientDao;
		this.ligneCommandeFournisseurDao = ligneCommandeFournisseurDao;
	}

	@Override
	public ArticleDtos save(ArticleDtos dto) {
		//
		List<String> errors = ArticleValidator.validate(dto);
		  if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", dto);
		      throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
		    }
		return ArticleDtos.fromEntity(
				articleDao.save(
						ArticleDtos.toEntity(dto)
						)
				);
	}

	@Override
	public ArticleDtos findById(Integer id) {
		if(id == null) {
			return null;
		}
		
		return articleDao.findById(id).map(ArticleDtos::fromEntity).orElseThrow(
				() -> new EntityNotfoundException("Aucun article avec l'ID = " + id + " n' ete trouve dans la BDD",
			            ErrorCodes.ARTICLE_NOT_FOUND)
				);
	}

	@Override
	public ArticleDtos findByCodeArticle(String codeArticle) {
		 if (!StringUtils.hasLength(codeArticle)) {
		      log.error("Article CODE is null");
		      return null;
		    }

		    return articleDao.findArticleByCodeArticle(codeArticle)
		    		.map(ArticleDtos::fromEntity)
		    		.orElseThrow(()->
		    		 new EntityNotfoundException("Aucun article avec le CODE = " + codeArticle + " n' ete trouve dans la BDD",
					            ErrorCodes.ARTICLE_NOT_FOUND)
		    				);
		    
	}
	
	//liste articles
	@Override
	public List<ArticleDtos> findAll() {
		
		return articleDao.findAll().stream()
				.map(ArticleDtos::fromEntity)
				.collect(Collectors.toList())
				;
	}

	//
	@Override
	public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
		
		return ligneVentesDao.findAllByArticleId(idArticle)
				.stream()
				.map(LigneVenteDto::fromEntity)
				.collect(Collectors.toList())
				;
	}

	
	//lignes de commandes
	@Override
	public List<LigneCommandeClientDto> findHistoriaueCommandeClient(Integer idArticle) {
		
		return ligneCommandeClientDao.findAllByCommandeClientId(idArticle)
				.stream()
				.map(LigneCommandeClientDto::fromEntity)
				.collect(Collectors.toList())
				;
	}
	
	//les commandes fournisseurs
	@Override
	public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
		
		return ligneCommandeFournisseurDao.findAllByCommandeFournisseurId(idArticle)
				.stream()
				.map(LigneCommandeFournisseurDto::fromEntity)
				.collect(Collectors.toList())
				;
	}

	@Override
	public List<ArticleDtos> findAllArticleByIdCategory(Integer idCategory) {
		
		return articleDao.findAllByCategoryId(idCategory)
				.stream()
				.map(ArticleDtos::fromEntity)
				.collect(Collectors.toList())
				;
	}
	
	//supprimer article
	@Override
	public void delete(Integer id) {
		 if (id == null) {
		      log.error("Article ID is null");
		      return;
		    }
		    List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientDao.findAllByArticleId(id);
		    if (!ligneCommandeClients.isEmpty()) {
		      throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCodes.ARTICLE_ALREADY_IN_USE);
		    }
		    List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurDao.findAllByArticleId(id);
		    if (!ligneCommandeFournisseurs.isEmpty()) {
		      throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes fournisseur",
		          ErrorCodes.ARTICLE_ALREADY_IN_USE);
		    }
		    List<LigneVente> ligneVentes = ligneVentesDao.findAllByArticleId(id);
		    if (!ligneVentes.isEmpty()) {
		      throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des ventes",
		          ErrorCodes.ARTICLE_ALREADY_IN_USE);
		    }
		    articleDao.deleteById(id);
		  }
		
	

}
