package com.apirest.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import com.apirest.domain.ValidationGroup;
import com.apirest.domain.exception.NegocioException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
@Entity
public class OrdemServico {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/*usando o valid para validar 
	 * os atributos obrigatorios dentro de cliente
	 * @ConvertGroup usando para criar um grupo de 
	 * validação para quando o @Valid for validar 
	 * cliente ele va somente nas validações que estiver 
	 * associada ao grupo e não nas demais validações.
	 * o Deafulta.class é substituido no momento da validação
	 * para somente o grupo espeficicado para tal.
	 */
	@Valid
	@ConvertGroup(from = Default.class, to = ValidationGroup.ClienteId.class)
	@ManyToOne
	@NotNull
	private Cliente cliente;
	
	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal preco;
	
	/*usando @Enumerated(EnumType.STRING)
	 *para informa que no campo da tabela no db
	 *ira aparecer a string do enum
	 *usando o @JsonProperty(acess = acess.READ_ONLY)
	 *usando o read_only para informa que o acesso é somente leitura 
	 *@JsonProperty é um metadado para informar ao serializador como objeto serial.
	 *É usado para: nome variável. acesso (LER, ESCREVER) 
	 */
	@Enumerated(EnumType.STRING)
	@JsonProperty(access = Access.READ_ONLY)
	private StatusOrdemServico status;
	
	@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataAbertura;
	
	@JsonProperty(access = Access.READ_ONLY)
	private OffsetDateTime dataFinalizada;
	
	@OneToMany(mappedBy = "ordemServico")
	private List<Comentario> comentarios = new ArrayList<Comentario>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public StatusOrdemServico getStatus() {
		return status;
	}
	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}
	public OffsetDateTime getDataAbertura() {
		return dataAbertura;
	}
	public void setDataAbertura(OffsetDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	public OffsetDateTime getDataFinalizada() {
		return dataFinalizada;
	}
	public void setDataFinalizada(OffsetDateTime dataFinalizada) {
		this.dataFinalizada = dataFinalizada;
	}
	public List<Comentario> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdemServico other = (OrdemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public boolean naoPoderserCancelada() {
		return !podeSerCancelada();
	}
	public boolean podeSerCancelada() {
		return StatusOrdemServico.ABERTA.equals(getStatus()) 
				|| StatusOrdemServico.FINALIZADA.equals(getStatus()) ;
	}
	public boolean podeSerFinalizada() {
		return StatusOrdemServico.ABERTA.equals(getStatus());
	}
	
	public boolean naoPodeSerfinalizada() {
		return !podeSerFinalizada();
	}
	public void finalizar() {
		if(naoPodeSerfinalizada()) {
			throw new NegocioException("Ordem de serviço não pode ser finalizada");
		}
		setStatus(StatusOrdemServico.FINALIZADA);
		setDataFinalizada(OffsetDateTime.now());
	}
	public void cancelada() {
		if(naoPoderserCancelada()) {
			throw new NegocioException("Ordem de serviço não pode ser cancelada");
		}
		setStatus(StatusOrdemServico.CANCELADA);
		setDataFinalizada(OffsetDateTime.now());		
	}
	
	
}
