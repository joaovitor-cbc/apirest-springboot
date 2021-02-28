package com.apirest.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apirest.domain.model.OrdemServico;

@Repository
public interface OrdemServiceRepository extends JpaRepository<OrdemServico, Long>{
	
}