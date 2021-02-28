package com.apirest.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.domain.exception.NegocioException;
import com.apirest.domain.model.Cliente;
import com.apirest.domain.repository.ClienteRepository;

/*Essa annotation serve para determinar as classes de serviço 
 * da aplicação. Geralmente é nessas classes que são injetados 
 * os DAOs e feito as operações de inserção, deleção, atualização, etc.
 * Já nas classes DAO é comum ser usado a anotação @Repository que define
 * as classes responsáveis da camada de persistência.
 */
@Service
public class CadastroClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente salvar(Cliente cliente) {
		Cliente clienteexistente = clienteRepository.findByEmail(cliente.getEmail());
		
		if(clienteexistente != null && !clienteexistente.equals(cliente)) {
			throw new NegocioException("Já existe um cliente cadastrado com esse e-mail");
		}
		return clienteRepository.save(cliente);
	}
	
	public void excluir(Long clienteId) {
		clienteRepository.deleteById(clienteId);
	}
	
}
