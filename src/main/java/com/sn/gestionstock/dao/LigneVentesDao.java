package com.sn.gestionstock.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.LigneVente;

@Repository
public interface LigneVentesDao extends JpaRepository<LigneVente, Integer> {

	  List<LigneVente> findAllByArticleId(Integer idArticle);

	  List<LigneVente> findAllByVenteId(Integer id);
}
