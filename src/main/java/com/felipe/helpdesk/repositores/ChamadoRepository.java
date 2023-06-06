package com.felipe.helpdesk.repositores;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipe.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer> {

}
