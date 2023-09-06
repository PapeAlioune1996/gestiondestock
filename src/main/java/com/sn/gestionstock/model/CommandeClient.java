package com.sn.gestionstock.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "commandeClient")
public class CommandeClient extends AbstractEntity{

	  @Column(name = "code")
	  private String code;

	  @Column(name = "datecommande")
	  private Instant dateCommande;

	  @Column(name = "etatcommande")
	  @Enumerated(EnumType.STRING)
	  private Etatcommande etatCommande;

	  @Column(name = "identreprise")
	  private Integer idEntreprise;

	  @ManyToOne
	  @JoinColumn(name = "idclient")
	  private Client client;

	  @OneToMany(mappedBy = "commandeClient")
	  private List<LigneCommandeClient> ligneCommandeClients;

}
