package com.apirest.domain.Controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.domain.model.Cliente;
import com.apirest.domain.repository.ClienteRepository;
import com.apirest.domain.service.CadastroClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	CadastroClienteService cadastroCliente;

	@GetMapping("/listaclientes")
	public List<Cliente> listaCliente() {
		return clienteRepository.findAll();
	}

	/*
	 * ResponseEntity: significa representar toda a resposta HTTP. Você pode
	 * controlar qualquer coisa que aconteça: código de status, cabeçalhos e corpo.
	 * O Optional é um recurso no mundo Java, tenha em mente como uma espécie de
	 * caixa que pode ser preenchida ou não, caso possua um algo então é possível
	 * utilizar vários recursos que nos auxiliam no manuseio desse conteúdo, caso a
	 * caixa não esteja preenchida ao menos podemos evitar,
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		return ResponseEntity.notFound().build();
	}

	/*
	 * @ResponseBody é um marcador para o corpo da resposta HTTP e @ResponseStatus
	 * declara o código de status da resposta HTTP. @ResponseStatus não é muito
	 * flexível. Ele marca todo o método, assim você deve ter certeza de que seu
	 * método manipulador sempre se comportará da mesma maneira.
	 * a anotação @Valid no parâmetro do nosso controller. Essa anotação serve 
	 * para indicar que o objeto será validado tendo como base as anotações de 
	 * validação que atribuímos aos campos.
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return cadastroCliente.salvar(cliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> alterar(@PathVariable Long id, @RequestBody Cliente cliente) {
		if (!clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		cliente.setId(id);
		cliente = cadastroCliente.salvar(cliente);
		return ResponseEntity.ok(cliente);
	}

	/*
	 * usando o ResponseEntity.noContent().build(); para retorna o codigo de sjtatus
	 * correto de exclusão caso o cliente exista porem o retorno não contem corpo na
	 * resposta, caso ele nã exista retorna ResponseEntity.notFound().build() com o
	 * codigo de status 404.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		if (!clienteRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		cadastroCliente.excluir(id);;
		;

		return ResponseEntity.noContent().build();
	}
}
