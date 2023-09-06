package com.sn.gestionstock.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.LigneCommandeClient;

@Repository
public interface LigneCommandeClientDao extends JpaRepository<LigneCommandeClient, Integer>{


	  List<LigneCommandeClient> findAllByCommandeClientId(Integer id);

	  List<LigneCommandeClient> findAllByArticleId(Integer id);
}
