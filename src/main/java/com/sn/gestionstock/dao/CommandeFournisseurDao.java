package com.sn.gestionstock.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.CommandeFournisseur;

@Repository
public interface CommandeFournisseurDao extends JpaRepository<CommandeFournisseur, Integer> {

	  Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);

	  List<CommandeFournisseur> findAllByFournisseurId(Integer id);
}
