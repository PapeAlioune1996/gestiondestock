package com.sn.gestionstock.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "mvtstock")
public class MvtStk extends AbstractEntity{


	  @Column(name = "datemvt")
	  private Instant dateMvt;

	  @Column(name = "quantite")
	  private BigDecimal quantite;

	  @ManyToOne
	  @JoinColumn(name = "idarticle")
	  private Article article;

	  @Column(name = "typemvt")
	  @Enumerated(EnumType.STRING)
	  private TypeMvtStk typeMvt;

	  @Column(name = "sourcemvt")
	  @Enumerated(EnumType.STRING)
	  private SourceMvtStck sourceMvt;

	  @Column(name = "identreprise")
	  private Integer idEntreprise;
	
	
}
