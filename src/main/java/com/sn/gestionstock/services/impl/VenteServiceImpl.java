package com.sn.gestionstock.services.impl;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.ArticleDao;
import com.sn.gestionstock.dao.LigneVentesDao;
import com.sn.gestionstock.dao.MvtstockDao;
import com.sn.gestionstock.dao.VenteDao;
import com.sn.gestionstock.dtos.ArticleDtos;
import com.sn.gestionstock.dtos.LigneVenteDto;
import com.sn.gestionstock.dtos.MvtStockDto;
import com.sn.gestionstock.dtos.VenteDto;
import com.sn.gestionstock.model.Article;
import com.sn.gestionstock.model.LigneVente;
import com.sn.gestionstock.model.SourceMvtStck;
import com.sn.gestionstock.model.TypeMvtStk;
import com.sn.gestionstock.model.Ventes;
import com.sn.gestionstock.services.MvtStockService;
import com.sn.gestionstock.services.VenteService;
import com.sn.gestionstock.validator.VentesValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VenteServiceImpl implements VenteService{
	
	private ArticleDao articleDao;
	
	private VenteDao venteDao;
	
	private LigneVentesDao ligneVentesDao;
	
	private MvtStockService mvtStockService;
	
	@Autowired
	public VenteServiceImpl(ArticleDao articleDao, VenteDao venteDao, LigneVentesDao ligneVentesDao,
			MvtStockService mvtStockService) {
		
		this.articleDao = articleDao;
		this.venteDao = venteDao;
		this.ligneVentesDao = ligneVentesDao;
		this.mvtStockService = mvtStockService;
	}

	@Override
	public VenteDto save(VenteDto dto) {

	    List<String> errors = VentesValidator.validate(dto);
	    if (!errors.isEmpty()) {
	      log.error("Ventes n'est pas valide");
	      throw new InvalidEntityException("L'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
	    }

	    List<String> articleErrors = new ArrayList<>();

	    dto.getLigneVentes().forEach(ligneVenteDto -> {
	      Optional<Article> article = articleDao.findById(ligneVenteDto.getArticle().getId());
	      if (article.isEmpty()) {
	        articleErrors.add("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a ete trouve dans la BDD");
	      }
	    });

	    if (!articleErrors.isEmpty()) {
	      log.error("One or more articles were not found in the DB, {}", errors);
	      throw new InvalidEntityException("Un ou plusieurs articles n'ont pas ete trouve dans la BDD", ErrorCodes.VENTE_NOT_VALID, errors);
	    }

	    Ventes savedVentes = venteDao.save(VenteDto.toEntity(dto));

	    dto.getLigneVentes().forEach(ligneVenteDto -> {
	      LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
	      ligneVente.setVente(savedVentes);
	      ligneVentesDao.save(ligneVente);
	      updateMvtStk(ligneVente);
	    });

	    return VenteDto.fromEntity(savedVentes);
	}

	//find vente by id
	@Override
	public VenteDto findById(Integer id) {
		if (id == null) {
		      log.error("Ventes ID is NULL");
		      return null;
		    }
		    return venteDao.findById(id)
		        .map(VenteDto::fromEntity)
		        .orElseThrow(() -> new EntityNotfoundException("Aucun vente n'a ete trouve dans la BDD",
			            ErrorCodes.VENTE_NOT_FOUND)
		        		
		        		);
		  
	}

	@Override
	public VenteDto findByCode(String code) {
		   if (!StringUtils.hasLength(code)) {
			      log.error("Vente CODE is NULL");
			      return null;
			    }
			    return venteDao.findVentesByCode(code)
			        .map(VenteDto::fromEntity)
			        .orElseThrow(() -> new EntityNotfoundException("Aucune vente client n'a ete trouve avec le CODE " + code + " n' ete trouve dans la BDD",
				            ErrorCodes.VENTE_NOT_VALID)
			        );
	}

	@Override
	public List<VenteDto> findAll() {
		return venteDao.findAll().stream()
		        .map(VenteDto::fromEntity)
		        .collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
	    if (id == null) {
	        log.error("Vente ID is NULL");
	        return;
	      }
	      List<LigneVente> ligneVentes = ligneVentesDao.findAllByVenteId(id);
	      if (!ligneVentes.isEmpty()) {
	        throw new InvalidOperationException("Impossible de supprimer une vente ...",
	            ErrorCodes.VENTE_ALREADY_IN_USE);
	      }
	      venteDao.deleteById(id);
		
	}
	
	//methode personnaliser
	  private void updateMvtStk(LigneVente lig) {
		    MvtStockDto mvtStkDto = MvtStockDto.builder()
		        .article(ArticleDtos.fromEntity(lig.getArticle()))
		        .dateMvt(Instant.now())
		        .typeMvt(TypeMvtStk.SORTIE)
		        .sourceMvt(SourceMvtStck.VENTE)
		        .quantite(lig.getQuantite())
		        .idEntreprise(lig.getIdEntreprise())
		        .build();
		    mvtStockService.sortieStock(mvtStkDto);
		  }

}
