package com.sn.gestionstock.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sn.gestionstock.Exception.EntityNotfoundException;
import com.sn.gestionstock.Exception.ErrorCodes;
import com.sn.gestionstock.Exception.InvalidEntityException;
import com.sn.gestionstock.Exception.InvalidOperationException;
import com.sn.gestionstock.dao.ClientDao;
import com.sn.gestionstock.dao.CommandeClientDao;
import com.sn.gestionstock.dtos.ClientDto;
import com.sn.gestionstock.model.CommandeClient;
import com.sn.gestionstock.services.ClientService;
import com.sn.gestionstock.validator.ClientValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService{
	
	private ClientDao clientDao;
	
	private CommandeClientDao commandeClientDao;
	
	
	@Autowired
	public ClientServiceImpl(ClientDao clientDao, CommandeClientDao commandeClientDao) {
		
		this.clientDao = clientDao;
		this.commandeClientDao = commandeClientDao;
	}

	@Override
	public ClientDto save(ClientDto dto) {
		  List<String> errors = ClientValidator.validate(dto);
		    if (!errors.isEmpty()) {
		      log.error("Client is not valid {}", dto);
		      throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
		    }

		    return ClientDto.fromEntity(
		    		clientDao.save(
		            ClientDto.toEntity(dto)
		        )
		    );
	}

	@Override
	public ClientDto findById(Integer id) {
		 if (id == null) {
		      log.error("Client ID is null");
		      return null;
		    }
		    return clientDao.findById(id)
		        .map(ClientDto::fromEntity)
		        .orElseThrow(() -> new EntityNotfoundException("Aucune category avec l'ID " + id + " n' ete trouve dans la BDD",
			            ErrorCodes.CLIENT_NOT_FOUND)
		        );
	}

	//liste des clients
	@Override
	public List<ClientDto> findAll() {
		 return clientDao.findAll().stream()
			        .map(ClientDto::fromEntity)
			        .collect(Collectors.toList());
	}

	//supprimer client
	@Override
	public void delete(Integer id) {
	    if (id == null) {
	        log.error("Client ID is null");
	        return;
	      }
	      List<CommandeClient> commandeClients = commandeClientDao.findAllByClientId(id);
	      if (!commandeClients.isEmpty()) {
	        throw new InvalidOperationException("Impossible de supprimer un client qui a deja des commande clients",
	            ErrorCodes.CLIENT_ALREADY_IN_USE);
	      }
	      clientDao.deleteById(id);
	    }
		
	

}
