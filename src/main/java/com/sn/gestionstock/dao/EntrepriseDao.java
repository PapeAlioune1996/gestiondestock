package com.sn.gestionstock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Entreprise;

@Repository
public interface EntrepriseDao extends JpaRepository<Entreprise, Integer> {
	

}
