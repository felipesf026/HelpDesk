package com.felipe.helpdesk.services;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipe.helpdesk.domain.Pessoa;
import com.felipe.helpdesk.domain.Tecnico;
import com.felipe.helpdesk.domain.dtos.TecnicoDTO;
import com.felipe.helpdesk.repositores.PessoaRepository;
import com.felipe.helpdesk.repositores.TecnicoRepository;
import com.felipe.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.felipe.helpdesk.services.exceptions.ObjectnotFoundException;


@Service
public class TecnicoService {

	@Autowired
	private TecnicoRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;


	public Tecnico findById(Integer id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto Não encontrado! id: " + id));

	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	}
	// CREATE 
	public Tecnico create(TecnicoDTO objDTO) {
		// asegurar que o id vai nulo

		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		validaPorCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}

	// UPDATE
	public Tecnico update(Integer id, @Valid TecnicoDTO objDto) {
		objDto.setId(id);
		Tecnico oldObj = findById(id);
		validaPorCpfEEmail(objDto);
		oldObj = new Tecnico(objDto);
		return repository.save(oldObj);
	}

	// DELETE
	public void delete(Integer id) {
		Tecnico obj = findById(id);
		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado!");
		}
		repository.deleteById(id);
	}

	// VALIDAÇÕES
	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
		}

		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if (obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
		}
	}

}