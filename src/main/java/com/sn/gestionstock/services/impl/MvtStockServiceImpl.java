package com.sn.gestionstock.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.dao.ArticleDao;
import com.sn.gestionstock.dao.MvtstockDao;
import com.sn.gestionstock.dtos.MvtStockDto;
import com.sn.gestionstock.model.TypeMvtStk;
import com.sn.gestionstock.services.MvtStockService;
import com.sn.gestionstock.validator.MvtStkValidator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MvtStockServiceImpl implements MvtStockService{
	
	private MvtstockDao mvtstockDao;
	
	private ArticleDao articleDao;
	
	
	@Autowired
	public MvtStockServiceImpl(MvtstockDao mvtstockDao, ArticleDao articleDao) {
		
		this.mvtstockDao = mvtstockDao;
		this.articleDao = articleDao;
	}

	@Override
	public BigDecimal stockReelArticle(Integer idArticle) {
		   if (idArticle == null) {
			      log.warn("ID article is NULL");
			      return BigDecimal.valueOf(-1);
			    }
			    articleDao.findById(idArticle);
			    return mvtstockDao.stockReelArticle(idArticle);
	}

	@Override
	public List<MvtStockDto> mvtStkArticle(Integer idArticle) {
		 return mvtstockDao.findAllByArticleId(idArticle).stream()
			        .map(MvtStockDto::fromEntity)
			        .collect(Collectors.toList());
	}

	@Override
	public MvtStockDto entreeStock(MvtStockDto dto) {
		return entreePositive(dto, TypeMvtStk.ENTREE);
	}

	@Override
	public MvtStockDto sortieStock(MvtStockDto dto) {
		return sortieNegative(dto, TypeMvtStk.SORTIE);
	}

	@Override
	public MvtStockDto correctionStockPos(MvtStockDto dto) {
		return entreePositive(dto, TypeMvtStk.CORRECTION_POS);
	}

	@Override
	public MvtStockDto correctionStockNeg(MvtStockDto dto) {
		return sortieNegative(dto, TypeMvtStk.CORRECTION_NEG);
	}

	
	///
	  private MvtStockDto entreePositive(MvtStockDto dto, TypeMvtStk typeMvtStk) {
		    List<String> errors = MvtStkValidator.validate(dto);
		    if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", dto);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
		    }
		    dto.setQuantite(
		        BigDecimal.valueOf(
		            Math.abs(dto.getQuantite().doubleValue())
		        )
		    );
		    dto.setTypeMvt(typeMvtStk);
		    return MvtStockDto.fromEntity(
		        mvtstockDao.save(MvtStockDto.toEntity(dto))
		    );
		  }

		  private MvtStockDto sortieNegative(MvtStockDto dto, TypeMvtStk typeMvtStk) {
		    List<String> errors = MvtStkValidator.validate(dto);
		    if (!errors.isEmpty()) {
		      log.error("Article is not valid {}", dto);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
		    }
		    dto.setQuantite(
		        BigDecimal.valueOf(
		            Math.abs(dto.getQuantite().doubleValue()) * -1
		        )
		    );
		    dto.setTypeMvt(typeMvtStk);
		    return MvtStockDto.fromEntity(
		        mvtstockDao.save(MvtStockDto.toEntity(dto))
		    );
		  }
}
