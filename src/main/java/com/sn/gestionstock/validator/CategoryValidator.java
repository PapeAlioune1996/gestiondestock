package com.sn.gestionstock.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.dtos.CategoryDto;

public class CategoryValidator {

  public static List<String> validate(CategoryDto categoryDto) {
    List<String> errors = new ArrayList<>();

    if (categoryDto == null || !StringUtils.hasLength(categoryDto.getCode())) {
      errors.add("Veuillez renseigner le code de la categorie");
    }
    return errors;
  }

}
