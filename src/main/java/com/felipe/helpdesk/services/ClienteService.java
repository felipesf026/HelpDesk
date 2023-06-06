package com.felipe.helpdesk.services;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipe.helpdesk.domain.Pessoa;
import com.felipe.helpdesk.domain.dtos.ClienteDTO;
import com.felipe.helpdesk.domain.Cliente;
import com.felipe.helpdesk.repositores.PessoaRepository;
import com.felipe.helpdesk.repositores.ClienteRepository;
import com.felipe.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.felipe.helpdesk.services.exceptions.ObjectnotFoundException;


@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotFoundException("Objeto Não encontrado! id: " + id));

	}

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	// CREATE
	public Cliente create(ClienteDTO objDTO) {
		// asegurar que o id vai nulo

		objDTO.setId(null);
		objDTO.setSenha(encoder.encode(objDTO.getSenha()));
		
		validaPorCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return repository.save(newObj);
	}

	// UPDATE
	public Cliente update(Integer id, @Valid ClienteDTO objDto) {
		objDto.setId(id);
		Cliente oldObj = findById(id);
		validaPorCpfEEmail(objDto);
		oldObj = new Cliente(objDto);
		return repository.save(oldObj);
	}

	// DELETE
	public void delete(Integer id) {
		Cliente obj = findById(id);
		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
		}
		repository.deleteById(id);
	}

	// VALIDAÇÕES
	private void validaPorCpfEEmail(ClienteDTO objDTO) {
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