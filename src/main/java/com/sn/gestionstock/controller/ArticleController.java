package com.sn.gestionstock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.ArticleDtos;
import com.sn.gestionstock.dtos.LigneCommandeClientDto;
import com.sn.gestionstock.dtos.LigneCommandeFournisseurDto;
import com.sn.gestionstock.dtos.LigneVenteDto;
import com.sn.gestionstock.model.LigneCommandeClient;
import com.sn.gestionstock.services.ArticleService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class ArticleController {
	
	private ArticleService articleService;

	@Autowired
	public ArticleController(ArticleService articleService) {
		
		this.articleService = articleService;
	}
	
	//ajouter article
	 @PostMapping(value="/articles/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArticleDtos save(@RequestBody ArticleDtos dto) {
		return articleService.save(dto);
	}
	 
	 //liste article
	  @GetMapping(value = "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<ArticleDtos> listarticle(){
		 return articleService.findAll();
	 }
	  
	  //find article by code article
	  @GetMapping(value = "/articles/filter/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public ArticleDtos findbycodearticle(@PathVariable("codeArticle") String codeArticle) {
		  return articleService.findByCodeArticle(codeArticle);
	  }
	  
	  //historique ventes par id
	  @GetMapping( value="/articles/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public List<LigneVenteDto> historiqueventesByIdArticle(@PathVariable("idArticle") Integer idArticle){
		  return articleService.findHistoriqueVentes(idArticle);
	  }

	  //historique commande client
	  @GetMapping(value ="/articles/historique/commandeclient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public List<LigneCommandeClientDto> historiqueCommandeClient(@PathVariable("idArticle") Integer idArticle) {
		  return articleService.findHistoriaueCommandeClient(idArticle);
	  }
	  
	  //historique commande fournisseur
	  @GetMapping(value = "/articles/historique/commandefournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public List<LigneCommandeFournisseurDto> historiquecommandefournisseur(@PathVariable("idArticle") Integer idArticle){
		  return articleService.findHistoriqueCommandeFournisseur(idArticle);
	  }
	  
	  //liste d'articles par category
	  @GetMapping(value = "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
	  public List<ArticleDtos> findCategoryArticle(@PathVariable("idCategory") Integer idCategory){
		  return articleService.findAllArticleByIdCategory(idCategory);
	  }
	  
	  //supprimer article
	  @DeleteMapping(value =  "/articles/delete/{idArticle}")
	  public void deletearticle(@PathVariable("idArticle") Integer idArticle) {
		  articleService.delete(idArticle);
	  }
	
	

}
