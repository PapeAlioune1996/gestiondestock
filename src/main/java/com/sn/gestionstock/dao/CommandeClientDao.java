package com.sn.gestionstock.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.CommandeClient;

@Repository
public interface CommandeClientDao extends JpaRepository<CommandeClient, Integer> {
	
	  Optional<CommandeClient> findCommandeClientByCode(String code);

	  List<CommandeClient> findAllByClientId(Integer id);
}
