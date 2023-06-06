package com.felipe.helpdesk.repositores;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipe.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer> {

}
