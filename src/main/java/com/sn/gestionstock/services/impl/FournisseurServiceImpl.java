package com.sn.gestionstock.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.CommandeFournisseurDao;
import com.sn.gestionstock.dao.FournisseurDao;
import com.sn.gestionstock.dtos.FournisseurDto;
import com.sn.gestionstock.model.CommandeClient;
import com.sn.gestionstock.model.CommandeFournisseur;
import com.sn.gestionstock.services.FournisseurService;
import com.sn.gestionstock.validator.FournisseurValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FournisseurServiceImpl  implements FournisseurService{
	
	
	private FournisseurDao fournisseurDao;
	
	private CommandeFournisseurDao commandeFournisseurDao;
	
	@Autowired
	public FournisseurServiceImpl(FournisseurDao fournisseurDao, CommandeFournisseurDao commandeFournisseurDao) {
		
		this.fournisseurDao = fournisseurDao;
		this.commandeFournisseurDao = commandeFournisseurDao;
	}

	@Override
	public FournisseurDto save(FournisseurDto dto) {
	    List<String> errors = FournisseurValidator.validate(dto);
	    if (!errors.isEmpty()) {
	      log.error("Fournisseur is not valid {}", dto);
	      throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
	    }

	    return FournisseurDto.fromEntity(
	        fournisseurDao.save(
	            FournisseurDto.toEntity(dto)
	        )
	    );
	}

	//
	@Override
	public FournisseurDto findById(Integer id) {
	    if (id == null) {
	        log.error("Fournisseur ID is null");
	        return null;
	      }
	      return fournisseurDao.findById(id)
	          .map(FournisseurDto::fromEntity)
	          .orElseThrow(() -> new EntityNotfoundException("Aucun fournisseur avec l'ID" + id + " n' ete trouve dans la BDD",
			            ErrorCodes.FOURNISSEUR_NOT_FOUND)
	          );
	}
	//

	@Override
	public List<FournisseurDto> findAll() {
		return fournisseurDao.findAll().stream()
		        .map(FournisseurDto::fromEntity)
		        .collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {

	    if (id == null) {
	      log.error("Fournisseur ID is null");
	      return;
	    }
	    List<CommandeFournisseur> commandeFournisseur = commandeFournisseurDao.findAllByFournisseurId(id);
	    if (!commandeFournisseur.isEmpty()) {
	      throw new InvalidOperationException("Impossible de supprimer un fournisseur qui a deja des commandes",
	          ErrorCodes.FOURNISSEUR_ALREADY_IN_USE);
	    }
	    fournisseurDao.deleteById(id);
		
	}

}
