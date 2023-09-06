package com.sn.gestionstock.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Adresse {
	
	private String adresse1;
	
	private String adresse2;
	
	private String ville;
	
	private String codepostal;
	
	private String pays;
	
	

}
