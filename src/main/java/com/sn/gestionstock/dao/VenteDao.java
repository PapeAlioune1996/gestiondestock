package com.sn.gestionstock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Ventes;

@Repository
public interface VenteDao extends JpaRepository<Ventes, Integer> {

	  Optional<Ventes> findVentesByCode(String code);
}
