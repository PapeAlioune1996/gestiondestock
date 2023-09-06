package com.sn.gestionstock.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.LigneCommandeFournisseur;

@Repository
public interface LigneCommandeFournisseurDao extends JpaRepository<LigneCommandeFournisseur, Integer> {
	  
	  List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Integer idCommande);

	  List<LigneCommandeFournisseur> findAllByArticleId(Integer idCommande);
}
