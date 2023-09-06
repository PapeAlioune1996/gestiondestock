package com.sn.gestionstock.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.MvtStockDto;
import com.sn.gestionstock.services.MvtStockService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class MvtStockController {
	
	private MvtStockService mvtStockService;

	@Autowired
	public MvtStockController(MvtStockService mvtStockService) {
		
		this.mvtStockService = mvtStockService;
	}
	
	

	  @GetMapping("/mvtstk/stockreel/{idArticle}")
	  public BigDecimal stockReelArticle(@PathVariable("idArticle") Integer idArticle) {
		  return mvtStockService.stockReelArticle(idArticle);
	  }

	  @GetMapping("/mvtstk/filter/article/{idArticle}")
	  public List<MvtStockDto> mvtStkArticle(@PathVariable("idArticle") Integer idArticle){
		  return mvtStockService.mvtStkArticle(idArticle);
	  }

	  @PostMapping("/mvtstk/entree")
	  public MvtStockDto entreeStock(@RequestBody MvtStockDto dto) {
		  return mvtStockService.entreeStock(dto);
	  }

	  @PostMapping("/mvtstk/sortie")
	  public MvtStockDto sortieStock(@RequestBody MvtStockDto dto) {
		  return mvtStockService.sortieStock(dto);
	  }

	  @PostMapping("/mvtstk/correctionpos")
	  public MvtStockDto correctionStockPos(@RequestBody MvtStockDto dto) {
		  return mvtStockService.correctionStockPos(dto);
	  }

	  @PostMapping("/mvtstk/correctionneg")
	  public MvtStockDto correctionStockNeg(@RequestBody MvtStockDto dto) {
		  return mvtStockService.correctionStockNeg(dto);
	  }

}
