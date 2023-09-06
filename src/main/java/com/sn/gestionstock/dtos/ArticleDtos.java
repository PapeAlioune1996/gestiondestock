package com.sn.gestionstock.dtos;

import java.math.BigDecimal;

import com.sn.gestionstock.model.Article;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArticleDtos {
	

	  private Integer id;

	  private String codeArticle;

	  private String designation;

	  private BigDecimal prixUnitaireHt;

	  private BigDecimal tauxTva;

	  private BigDecimal prixUnitaireTtc;

	  private String photo;

	  private CategoryDto category;

	  private Integer idEntreprise;

	  public static ArticleDtos fromEntity(Article article) {
	    if (article == null) {
	      return null;
	    }
	    return ArticleDtos.builder()
	        .id(article.getId())
	        .codeArticle(article.getCodeArticle())
	        .designation(article.getDesignation())
	        .photo(article.getPhoto())
	        .prixUnitaireHt(article.getPrixUnitaireHt())
	        .prixUnitaireTtc(article.getPrixUnitaireTtc())
	        .tauxTva(article.getTauxTva())
	        .idEntreprise(article.getIdEntreprise())
	        .category(CategoryDto.fromEntity(article.getCategory()))
	        .build();
	  }

	  public static Article toEntity(ArticleDtos articleDto) {
	    if (articleDto == null) {
	      return null;
	    }
	    Article article = new Article();
	    article.setId(articleDto.getId());
	    article.setCodeArticle(articleDto.getCodeArticle());
	    article.setDesignation(articleDto.getDesignation());
	    article.setPhoto(articleDto.getPhoto());
	    article.setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
	    article.setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
	    article.setTauxTva(articleDto.getTauxTva());
	    article.setIdEntreprise(articleDto.getIdEntreprise());
	    article.setCategory(CategoryDto.toEntity(articleDto.getCategory()));
	    return article;
	  }

}
