package com.sn.gestionstock.model;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "utilisateur")
public class Utilisateur extends AbstractEntity {

	  @Column(name = "nom")
	  private String nom;

	  @Column(name = "prenom")
	  private String prenom;

	  @Column(name = "email")
	  private String email;

	  @Column(name = "datedenaissance")
	  private Instant dateDeNaissance;

	  @Column(name = "motdepasse")
	  private String moteDePasse;

	  @Embedded
	  private Adresse adresse;

	  @Column(name = "photo")
	  private String photo;

	  @ManyToOne
	  @JoinColumn(name = "identreprise")
	  private Entreprise entreprise;


	  @OneToMany(fetch = FetchType.EAGER, mappedBy = "utilisateur")
	  @JsonIgnore
	  private List<Roles> roles;
}
