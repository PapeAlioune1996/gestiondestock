package com.sn.gestionstock.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.dtos.VenteDto;

public class VentesValidator {

  public static List<String> validate(VenteDto dto) {
    List<String> errors = new ArrayList<>();
    if (dto == null) {
      errors.add("Veuillez renseigner le code de la commande");
      errors.add("Veuillez renseigner la date de la commande");
      return errors;
    }

    if (!StringUtils.hasLength(dto.getCode())) {
      errors.add("Veuillez renseigner le code de la commande");
    }
    if (dto.getDateVente() == null) {
      errors.add("Veuillez renseigner la date de la commande");
    }

    return errors;
  }

}
