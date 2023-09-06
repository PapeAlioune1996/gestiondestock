package com.sn.gestionstock.services;

import java.util.List;

import com.sn.gestionstock.dtos.ArticleDtos;
import com.sn.gestionstock.dtos.LigneCommandeClientDto;
import com.sn.gestionstock.dtos.LigneCommandeFournisseurDto;
import com.sn.gestionstock.dtos.LigneVenteDto;

public interface ArticleService {
	
	  ArticleDtos save(ArticleDtos dto);

	  ArticleDtos findById(Integer id);

	  ArticleDtos findByCodeArticle(String codeArticle);

	  List<ArticleDtos> findAll();

	  List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);

	  List<LigneCommandeClientDto> findHistoriaueCommandeClient(Integer idArticle);

	  List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);

	  List<ArticleDtos> findAllArticleByIdCategory(Integer idCategory);

	  void delete(Integer id);

}
