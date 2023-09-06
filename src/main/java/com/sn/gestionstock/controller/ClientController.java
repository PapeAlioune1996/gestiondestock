package com.sn.gestionstock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sn.gestionstock.dtos.ClientDto;
import com.sn.gestionstock.services.ClientService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/gestionstock")
public class ClientController {
	
	private ClientService clientService;

	@Autowired
	public ClientController(ClientService clientService) {
		
		this.clientService = clientService;
	}
	
	//enregitrer client
	 @PostMapping(value =  "/clients/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ClientDto save(@RequestBody ClientDto clientDto) {
		return clientService.save(clientDto);
	}
	 
	//find client by id
	 @GetMapping(value = "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
	 public ClientDto findClientById(@PathVariable("idClient") Integer id) {
		 return clientService.findById(id);
		 
	 }
	 
	 //find all client
	 @GetMapping(value = "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
	 public List<ClientDto>  findAll() {
		 return clientService.findAll();
	 }
	
	//supprimer client
	  @DeleteMapping(value = "/clients/delete/{idClient}")
	 public void deleteclient(@PathVariable("idClient") Integer id) {
		 clientService.delete(id);
	 }

}
