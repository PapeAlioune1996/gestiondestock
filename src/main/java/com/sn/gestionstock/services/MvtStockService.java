package com.sn.gestionstock.services;

import java.math.BigDecimal;
import java.util.List;

import com.sn.gestionstock.dtos.MvtStockDto;

public interface MvtStockService {

	

		  BigDecimal stockReelArticle(Integer idArticle);

		  List<MvtStockDto> mvtStkArticle(Integer idArticle);

		  MvtStockDto entreeStock(MvtStockDto dto);

		  MvtStockDto sortieStock(MvtStockDto dto);

		  MvtStockDto correctionStockPos(MvtStockDto dto);

		  MvtStockDto correctionStockNeg(MvtStockDto dto);

}
