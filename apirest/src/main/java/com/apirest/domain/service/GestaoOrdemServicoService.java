package com.apirest.domain.service;

import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apirest.domain.exception.EntidadeNaoEncontradaException;
import com.apirest.domain.exception.NegocioException;
import com.apirest.domain.model.Cliente;
import com.apirest.domain.model.Comentario;
import com.apirest.domain.model.OrdemServico;
import com.apirest.domain.model.StatusOrdemServico;
import com.apirest.domain.repository.ClienteRepository;
import com.apirest.domain.repository.ComentarioRepository;
import com.apirest.domain.repository.OrdemServiceRepository;

@Service
public class GestaoOrdemServicoService {
	
	@Autowired
	OrdemServiceRepository ordemServiceRepository;
	
	@Autowired
	private ClienteRepository clienteRespository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = clienteRespository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		return ordemServiceRepository.save(ordemServico);
	}
	
	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		ordemServico.finalizar();
		ordemServiceRepository.save(ordemServico);
	}
	public void cancelar(long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		ordemServico.cancelada();
		ordemServiceRepository.save(ordemServico);
	}
	
	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);
		return comentarioRepository.save(comentario);
	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServiceRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem servico não encontrada"));
	}
}
