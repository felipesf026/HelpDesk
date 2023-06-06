package com.felipe.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.felipe.helpdesk.domain.Chamado;
import com.felipe.helpdesk.domain.dtos.ChamadoDTO;
import com.felipe.helpdesk.services.ChamadoService;


@RestController
@RequestMapping(value = "/chamados")
public class ChamadoResource {
	
	@Autowired
	private ChamadoService service;
	
	
	// BUSCAR POR ID
	@GetMapping(value= "/{id}")
	public ResponseEntity<ChamadoDTO> findById(@PathVariable Integer id){
		Chamado obj = service.findById(id);
		return ResponseEntity.ok().body(new ChamadoDTO(obj));
	}
	
	// BUSCAR TODOS
	@GetMapping
	public ResponseEntity<List<ChamadoDTO>> findAll(){
		List<Chamado> list = service.findAll();
		List<ChamadoDTO> listDTO = list.stream().map(obj -> new ChamadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	// CREATE
	@PostMapping
	public ResponseEntity<ChamadoDTO> create(@Valid @RequestBody ChamadoDTO objDto){
		Chamado obj = service.create(objDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
		
	}
	
	// UPDATE
	@PutMapping(value = "/{id}")
	public ResponseEntity<ChamadoDTO> update (@PathVariable Integer id, @Valid @RequestBody ChamadoDTO objDto ) {
		
		Chamado newObj = service.update(id, objDto);
		return ResponseEntity.ok().body(new ChamadoDTO(newObj));
		
	}
	
	

}