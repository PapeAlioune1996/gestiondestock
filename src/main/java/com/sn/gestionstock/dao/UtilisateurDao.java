package com.sn.gestionstock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sn.gestionstock.model.Utilisateur;

@Repository
public interface UtilisateurDao extends JpaRepository<Utilisateur, Integer> {
	
	  // JPQL query
	  @Query(value = "select u from Utilisateur u where u.email = :email")
	  Optional<Utilisateur> findUtilisateurByEmail(@Param("email") String email);

}
