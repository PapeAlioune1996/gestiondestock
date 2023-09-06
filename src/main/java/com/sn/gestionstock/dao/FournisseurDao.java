package com.sn.gestionstock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Fournisseur;

@Repository 
public interface FournisseurDao extends JpaRepository<Fournisseur, Integer> {

}
