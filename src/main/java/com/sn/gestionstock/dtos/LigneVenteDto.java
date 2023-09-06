package com.sn.gestionstock.dtos;



import java.math.BigDecimal;

import com.sn.gestionstock.model.LigneVente;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LigneVenteDto {
	

		  private Integer id;

		  private VenteDto vente;

		  private ArticleDtos article;

		  private BigDecimal quantite;

		  private BigDecimal prixUnitaire;

		  private Integer idEntreprise;

		  public static LigneVenteDto fromEntity(LigneVente ligneVente) {
		    if (ligneVente == null) {
		      return null;
		    }

		    return LigneVenteDto.builder()
		        .id(ligneVente.getId())
		        .vente(VenteDto.fromEntity(ligneVente.getVente()))
		        .article(ArticleDtos.fromEntity(ligneVente.getArticle()))
		        .quantite(ligneVente.getQuantite())
		        .prixUnitaire(ligneVente.getPrixUnitaire())
		        .idEntreprise(ligneVente.getIdEntreprise())
		        .build();
		  }

		  public static LigneVente toEntity(LigneVenteDto dto) {
		    if (dto == null) {
		      return null;
		    }
		    LigneVente ligneVente = new LigneVente();
		    ligneVente.setId(dto.getId());
		    ligneVente.setVente(VenteDto.toEntity(dto.getVente()));
		    ligneVente.setArticle(ArticleDtos.toEntity(dto.getArticle()));
		    ligneVente.setQuantite(dto.getQuantite());
		    ligneVente.setPrixUnitaire(dto.getPrixUnitaire());
		    ligneVente.setIdEntreprise(dto.getIdEntreprise());
		    return ligneVente;
		  }

}

