package com.sn.gestionstock.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.MvtStk;

@Repository
public interface MvtstockDao extends JpaRepository<MvtStk, Integer> {
	
	 @Query("select sum(m.quantite) from MvtStk m where m.article.id = :idArticle")
	  BigDecimal stockReelArticle(@Param("idArticle") Integer idArticle);

	  List<MvtStk> findAllByArticleId(Integer idArticle);

}
