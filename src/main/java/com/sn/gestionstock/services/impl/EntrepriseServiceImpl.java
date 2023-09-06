package com.sn.gestionstock.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.dao.EntrepriseDao;
import com.sn.gestionstock.dao.RoleDao;
import com.sn.gestionstock.dtos.EntrepriseDto;
import com.sn.gestionstock.dtos.RolesDto;
import com.sn.gestionstock.dtos.UtilisateurDto;
import com.sn.gestionstock.services.EntrepriseService;
import com.sn.gestionstock.services.UtilisateurService;
import com.sn.gestionstock.validator.EntrepriseValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService{
	
	private EntrepriseDao entrepriseDao;
	
	private UtilisateurService utilisateurService;
	
	private RoleDao roleDao;
	
	
	@Autowired
	public EntrepriseServiceImpl(EntrepriseDao entrepriseDao, UtilisateurService utilisateurService, RoleDao roleDao) {
		this.entrepriseDao = entrepriseDao;
		this.utilisateurService = utilisateurService;
		this.roleDao = roleDao;
	}

	@Override
	public EntrepriseDto save(EntrepriseDto dto) {

	    List<String> errors = EntrepriseValidator.validate(dto);
	    if (!errors.isEmpty()) {
	      log.error("Entreprise is not valid {}", dto);
	      throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
	    }
	    EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
	        entrepriseDao.save(EntrepriseDto.toEntity(dto))
	    );

	    UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);

	    UtilisateurDto savedUser = utilisateurService.save(utilisateur);

	    RolesDto rolesDto = RolesDto.builder()
	        .roleName("ADMIN")
	        .utilisateur(savedUser)
	        .build();

	    roleDao.save(RolesDto.toEntity(rolesDto));

	    return  savedEntreprise;
	}
	//
	  private UtilisateurDto fromEntreprise(EntrepriseDto dto) {
		    return UtilisateurDto.builder()
		        .adresse(dto.getAdresse())
		        .nom(dto.getNom())
		        .prenom(dto.getCodeFiscal())
		        .email(dto.getEmail())
		        .moteDePasse(generateRandomPassword())
		        .entreprise(dto)
		        .dateDeNaissance(Instant.now())
		        .photo(dto.getPhoto())
		        .build();
		  }
	//
	  private String generateRandomPassword() {
		    return "som3R@nd0mP@$$word";
		  }

	  //find entreprise by id
		  @Override
		  public EntrepriseDto findById(Integer id) {
		    if (id == null) {
		      log.error("Entreprise ID is null");
		      return null;
		    }
		    return entrepriseDao.findById(id)
		        .map(EntrepriseDto::fromEntity)
		        .orElseThrow(() -> new EntityNotfoundException("Aucune entreprise avec l'ID = " + id + " n' ete trouve dans la BDD",
			            ErrorCodes.ENTREPRISE_NOT_FOUND)
		        );
		  }

//listes des entreprises
	@Override
	public List<EntrepriseDto> findAll() {
		return entrepriseDao.findAll().stream()
		        .map(EntrepriseDto::fromEntity)
		        .collect(Collectors.toList());
	}

	//supprimer entreprise
	@Override
	public void delete(Integer id) {
		if (id == null) {
		      log.error("Entreprise ID is null");
		      return;
		    }
		    entrepriseDao.deleteById(id);
		
	}

}
