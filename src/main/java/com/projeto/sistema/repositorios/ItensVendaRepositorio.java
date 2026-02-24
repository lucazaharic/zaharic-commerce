package com.projeto.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.sistema.modelos.Cliente;
import com.projeto.sistema.modelos.ItensVenda;
import com.projeto.sistema.modelos.Venda;

public interface ItensVendaRepositorio extends JpaRepository <Venda, Long> {

	void saveAndFlush(ItensVenda it);

	
	
}
