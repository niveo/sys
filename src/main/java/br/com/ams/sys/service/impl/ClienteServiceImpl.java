package br.com.ams.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ams.sys.entity.Cliente;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.records.ClienteDto;
import br.com.ams.sys.records.ClienteListaDto;
import br.com.ams.sys.records.EnderecoDto;
import br.com.ams.sys.records.EstadoDto;
import br.com.ams.sys.repository.ClienteRepository;
import br.com.ams.sys.service.ClienteService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Cliente save(Cliente entidade) throws Exception {
		return clienteRepository.save(entidade);
	}

	@Override
	public Cliente findByCodigo(Long codigo) throws Exception {
		return this.clienteRepository.findById(codigo)
				.orElseThrow(() -> new EntityNotFoundException("Not entity found"));
	}

	@Override
	public void deleteByCodigo(Long codigo) throws Exception {
		this.clienteRepository.deleteById(codigo);
	}

	@Override
	public Page<ClienteListaDto> obterTodos(Pageable pageable) throws Exception {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ClienteListaDto.class);
		var root = query.from(Cliente.class);

		var select = cb.construct(ClienteListaDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("telefone"), root.get("email"), root.get("inscricaoEstadual"),
				root.get("tipoPessoa"));

		query.select(select);

		// Create Count Query
		var countQuery = cb.createQuery(Long.class);
		var clientesCount = countQuery.from(Cliente.class);
		countQuery.select(cb.count(clientesCount));
		var count = entityManager.createQuery(countQuery).getSingleResult();

		var registros = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize()).getResultList();

		return new PageImpl<ClienteListaDto>(registros, pageable, count);

	}

	public ClienteDto obterCodigo(Long codigo) throws Exception {
		var cb = entityManager.getCriteriaBuilder();
		var query = cb.createQuery(ClienteDto.class);
		var root = query.from(Cliente.class);

		var endereco = root.get("endereco");
		var cidade = endereco.get("cidade");
		var estado = cidade.get("estado");
		var bairro = endereco.get("bairro");

		var selectEstado = cb.construct(EstadoDto.class, estado.get("codigo"), estado.get("descricao"),
				estado.get("sigla"));

		var selectCidade = cb.construct(CidadeDto.class, cidade.get("codigo"), cidade.get("descricao"), selectEstado);

		var selectBairro = cb.construct(BairroDto.class, bairro.get("codigo"), bairro.get("descricao"));

		var selectEndereco = cb.construct(EnderecoDto.class, endereco.get("logradouro"), endereco.get("numero"),
				endereco.get("cep"), endereco.get("complemento"), selectCidade, selectBairro);

		var select = cb.construct(ClienteDto.class, root.get("codigo"), root.get("documento"), root.get("nome"),
				root.get("razaoSocial"), root.get("observacao"), root.get("telefone"), root.get("email"),
				root.get("inscricaoEstadual"), root.get("tipoPessoa"), selectEndereco);

		query.select(select);

		return (ClienteDto) entityManager.createQuery(query).getSingleResult();

	}
}
