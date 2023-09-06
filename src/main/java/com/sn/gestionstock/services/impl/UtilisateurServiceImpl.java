package com.sn.gestionstock.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.UtilisateurDao;
import com.sn.gestionstock.dtos.ChangerMotDePasseUtilisateurDto;
import com.sn.gestionstock.dtos.UtilisateurDto;
import com.sn.gestionstock.model.Utilisateur;
import com.sn.gestionstock.services.UtilisateurService;
import com.sn.gestionstock.validator.UtilisateurValidator;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {
	
	private UtilisateurDao utilisateurDao;
	
	
	@Autowired
	public UtilisateurServiceImpl(UtilisateurDao utilisateurDao) {
		super();
		this.utilisateurDao = utilisateurDao;
	}

	@Override
	public UtilisateurDto save(UtilisateurDto dto) {
	    List<String> errors = UtilisateurValidator.validate(dto);
	    if (!errors.isEmpty()) {
	      log.error("Utilisateur is not valid {}", dto);
	      throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
	    }

	    if(userAlreadyExists(dto.getEmail())) {
	      throw new InvalidEntityException("Un autre utilisateur avec le meme email existe deja", ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
	          Collections.singletonList("Un autre utilisateur avec le meme email existe deja dans la BDD"));
	    }


	    dto.setMoteDePasse(dto.getMoteDePasse());

	    return UtilisateurDto.fromEntity(
	    		utilisateurDao.save(
	            UtilisateurDto.toEntity(dto)
	        )
	    );
	}
	
	//verif email
	  private boolean userAlreadyExists(String email) {
		    Optional<Utilisateur> user = utilisateurDao.findUtilisateurByEmail(email);
		    return user.isPresent();
		  }

	  //find user by id
	@Override
	public UtilisateurDto findById(Integer id) {
	    if (id == null) {
	        log.error("Utilisateur ID is null");
	        return null;
	      }
	      return utilisateurDao.findById(id)
	          .map(UtilisateurDto::fromEntity)
	          .orElseThrow(() -> new EntityNotfoundException("Aucun utilisateur avec l'ID = " + id + " n' ete trouve dans la BDD",
			            ErrorCodes.UTILISATEUR_NOT_FOUND)
	          );
	}
	
	//liste des utilisateur
	@Override
	public List<UtilisateurDto> findAll() {
		return utilisateurDao.findAll().stream()
		        .map(UtilisateurDto::fromEntity)
		        .collect(Collectors.toList());
	}

	//supprimer un utilisateur
	@Override
	public void delete(Integer id) {
		 if (id == null) {
		      log.error("Utilisateur ID is null");
		      return;
		    }
		    utilisateurDao.deleteById(id);
		
	}

	@Override
	public UtilisateurDto findByEmail(String email) {
	    return utilisateurDao.findUtilisateurByEmail(email)
	            .map(UtilisateurDto::fromEntity)
	            .orElseThrow(() -> new EntityNotfoundException("Aucun utilisateur avec l'email = " + email + " n' ete trouve dans la BDD",
			            ErrorCodes.UTILISATEUR_NOT_FOUND)
	        );
	}

	//changer mot de passe
	@Override
	public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
	    validate(dto);
	    Optional<Utilisateur> utilisateurOptional = utilisateurDao.findById(dto.getId());
	    if (utilisateurOptional.isEmpty()) {
	      log.warn("Aucun utilisateur n'a ete trouve avec l'ID " + dto.getId());
	      throw new  EntityNotfoundException("Aucun utilisateur n'a ete trouve avec l'ID  " + dto.getId() + " n' ete trouve dans la BDD",
		            ErrorCodes.UTILISATEUR_NOT_FOUND);
	    }

	    Utilisateur utilisateur = utilisateurOptional.get();
	    utilisateur.setMoteDePasse(dto.getMotDePasse());

	    return UtilisateurDto.fromEntity(
	        utilisateurDao.save(utilisateur)
	    );
	}

	//
	  private void validate(ChangerMotDePasseUtilisateurDto dto) {
		    if (dto == null) {
		      log.warn("Impossible de modifier le mot de passe avec un objet NULL");
		      throw new InvalidOperationException("Aucune information n'a ete fourni pour pouvoir changer le mot de passe",
		          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		    }
		    if (dto.getId() == null) {
		      log.warn("Impossible de modifier le mot de passe avec un ID NULL");
		      throw new InvalidOperationException("ID utilisateur null:: Impossible de modifier le mote de passe",
		          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		    }
		    if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
		      log.warn("Impossible de modifier le mot de passe avec un mot de passe NULL");
		      throw new InvalidOperationException("Mot de passe utilisateur null:: Impossible de modifier le mote de passe",
		          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		    }
		    if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
		      log.warn("Impossible de modifier le mot de passe avec deux mots de passe different");
		      throw new InvalidOperationException("Mots de passe utilisateur non conformes:: Impossible de modifier le mote de passe",
		          ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		    }
		  }
}
