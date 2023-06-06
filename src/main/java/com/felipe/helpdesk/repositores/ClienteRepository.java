package com.felipe.helpdesk.repositores;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipe.helpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
