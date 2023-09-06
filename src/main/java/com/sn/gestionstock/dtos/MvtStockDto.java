
package com.sn.gestionstock.dtos;

import java.math.BigDecimal;
import java.time.Instant;

import com.sn.gestionstock.model.MvtStk;
import com.sn.gestionstock.model.SourceMvtStck;
import com.sn.gestionstock.model.TypeMvtStk;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class MvtStockDto {

	  private Integer id;

	  private Instant dateMvt;

	  private BigDecimal quantite;

	  private ArticleDtos article;

	  private TypeMvtStk typeMvt;

	  private SourceMvtStck sourceMvt;

	  private Integer idEntreprise;

	  public static MvtStockDto fromEntity(MvtStk mvtStk) {
	    if (mvtStk == null) {
	      return null;
	    }

	    return MvtStockDto.builder()
	        .id(mvtStk.getId())
	        .dateMvt(mvtStk.getDateMvt())
	        .quantite(mvtStk.getQuantite())
	        .article(ArticleDtos.fromEntity(mvtStk.getArticle()))
	        .typeMvt(mvtStk.getTypeMvt())
	        .sourceMvt(mvtStk.getSourceMvt())
	        .idEntreprise(mvtStk.getIdEntreprise())
	        .build();
	  }

	  public static MvtStk toEntity(MvtStockDto dto) {
	    if (dto == null) {
	      return null;
	    }

	    MvtStk mvtStk = new MvtStk();
	    mvtStk.setId(dto.getId());
	    mvtStk.setDateMvt(dto.getDateMvt());
	    mvtStk.setQuantite(dto.getQuantite());
	    mvtStk.setArticle(ArticleDtos.toEntity(dto.getArticle()));
	    mvtStk.setTypeMvt(dto.getTypeMvt());
	    mvtStk.setSourceMvt(dto.getSourceMvt());
	    mvtStk.setIdEntreprise(dto.getIdEntreprise());
	    return mvtStk;
	  }
	
}
