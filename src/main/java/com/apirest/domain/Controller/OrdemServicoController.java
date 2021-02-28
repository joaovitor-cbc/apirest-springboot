package com.apirest.domain.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import com.apirest.domain.repository.OrdemServiceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.apirest.domain.model.OrdemServico;
import com.apirest.domain.service.GestaoOrdemServicoService;
import com.apirest.model.OrdemServicoInput;
import com.apirest.model.OrdemServicoModel;

@RestController
@RequestMapping("/ordem-servico")
public class OrdemServicoController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServico;
	@Autowired
	private OrdemServiceRepository ordemServiceRepository;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServico criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		return gestaoOrdemServico.criar(ordemServico);
	}

	@GetMapping
	public List<OrdemServicoModel> listar(){
		return toCollectionModel(ordemServiceRepository.findAll());
	}
	/* Um framework que se destaca no mercado é o ModelMapper. 
	 * Com ele é possível mapear modelos complexos, com nenhuma 
	 * ou poucas configurações – sempre seguindo convenções. 
	 * Repare que o ModelMapper foi usado para mapear ordemServico
	 * foi mapeado para OrdemServicoModel.
	 */
	@GetMapping("/{ordemServicoId}")
	public ResponseEntity<OrdemServicoModel> ordemServico(@PathVariable Long ordemServicoId) {
		Optional<OrdemServico> ordemServico = ordemServiceRepository.findById(ordemServicoId);
		if(ordemServico.isPresent()) {
			OrdemServicoModel ordemServicoModel = toModel(ordemServico.get());
			return ResponseEntity.ok(ordemServicoModel);
		}
		return ResponseEntity.notFound().build()      ;
	}
	
	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServico.finalizar(ordemServicoId);
	}
	
	@PutMapping("/{ordemServicoId}/cancelamento")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServico.cancelar(ordemServicoId);
	}
	
	private OrdemServicoModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoModel.class);
	}
	private  List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordensServico){
		return ordensServico.stream()
				.map(ordemServico -> toModel(ordemServico))
				.collect(Collectors.toList());
	}
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
}
