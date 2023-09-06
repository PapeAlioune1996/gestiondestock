package com.sn.gestionstock.dtos;

import com.sn.gestionstock.model.Adresse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdresseDto {

	  private String adresse1;

	  private String adresse2;

	  private String ville;

	  private String codePostale;

	  private String pays;

	  public static AdresseDto fromEntity(Adresse adresse) {
	    if (adresse == null) {
	      return null;
	    }

	    return AdresseDto.builder()
	        .adresse1(adresse.getAdresse1())
	        .adresse2(adresse.getAdresse2())
	        .codePostale(adresse.getCodepostal())
	        .ville(adresse.getVille())
	        .pays(adresse.getPays())
	        .build();
	  }

	  public static Adresse toEntity(AdresseDto adresseDto) {
	    if (adresseDto == null) {
	      return null;
	    }
	    Adresse adresse = new Adresse();
	    adresse.setAdresse1(adresseDto.getAdresse1());
	    adresse.setAdresse2(adresseDto.getAdresse2());
	    adresse.setCodepostal(adresseDto.getCodePostale());
	    adresse.setVille(adresseDto.getVille());
	    adresse.setPays(adresseDto.getPays());
	    return adresse;
	  }
}
