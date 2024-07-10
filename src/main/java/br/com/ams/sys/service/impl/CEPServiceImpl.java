package br.com.ams.sys.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ams.sys.config.RedisConfig;
import br.com.ams.sys.entity.Bairro;
import br.com.ams.sys.entity.Cidade;
import br.com.ams.sys.records.BairroDto;
import br.com.ams.sys.records.CEPPesquisaDto;
import br.com.ams.sys.records.CepViaCepDto;
import br.com.ams.sys.records.CidadeCriarDto;
import br.com.ams.sys.records.CidadeDto;
import br.com.ams.sys.service.BairroService;
import br.com.ams.sys.service.CEPService;
import br.com.ams.sys.service.CidadeService;
import br.com.ams.sys.service.ClienteService;
import br.com.ams.sys.service.EstadoService;

@Service
public class CEPServiceImpl implements CEPService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BairroService bairroService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private ObjectMapper objectMapper;

	class CEPPesquisaDtoRowMapper implements RowMapper<CEPPesquisaDto> {

		@Override
		public CEPPesquisaDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				var cidade = cidadeService.obterCodigo(rs.getLong("cidade"));
				var bairro = bairroService.obterCodigo(rs.getLong("bairro"));
				var logradouro = rs.getString("logradouro");
				return CEPPesquisaDto.builder().logradouro(logradouro).bairro(bairro).cidade(cidade).build();
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException(e);
			}
		}

	}

	@Override
	@Cacheable(value = RedisConfig.CACHE_CEP_PESQUISA_KEY)
	public CEPPesquisaDto pesquisar(String cep) throws Exception {

		String cepTratado = cep.replace("-", "").trim();

		try {
			return jdbcTemplate
					.queryForObject("select c.cidade, c.bairro, c.logradouro FROM cliente as c WHERE c.cep = '"
							+ cepTratado + "' LIMIT 1", new CEPPesquisaDtoRowMapper());

		} catch (Exception e) {
			return pesquisarApiViaCep(cepTratado);
		}

	}

	private CEPPesquisaDto pesquisarApiViaCep(String cep) throws Exception {

		var defaultClient = RestClient.create();
		var result = defaultClient.get().uri("https://viacep.com.br/ws/{cep}/json", cep).retrieve()
				.toEntity(String.class);

		if (!result.getStatusCode().is2xxSuccessful())
			throw new Exception("Cep não localizado em viacep");

		var model = objectMapper.readValue(result.getBody(), CepViaCepDto.class);

		CidadeDto cidadeDto = null;
		try {
			cidadeDto = cidadeService.pesquisarDescricaoSingle(model.localidade(), model.uf());
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (cidadeDto == null) {
			var estado = estadoService.findBySigla(model.uf().toUpperCase());
			var cidade = cidadeService.save(Cidade.builder().descricao(model.localidade()).estado(estado).build());
			cidadeDto = cidade.toDto();
		}
		if (cidadeDto == null)
			throw new Exception("Não foi possivel obter a cidade para cep");

		BairroDto bairroDto = null;
		try {
			bairroDto = bairroService.pesquisarDescricaoSingle(model.bairro());
		} catch (Exception e) {
		}
		if (bairroDto == null) {
			var bairro = bairroService.save(Bairro.builder().descricao(model.bairro()).build());
			bairroDto = bairro.toDto();
		}
		if (bairroDto == null)
			throw new Exception("Não foi possivel obter o bairro para cep");

		return CEPPesquisaDto.builder().logradouro(model.logradouro()).bairro(bairroDto).cidade(cidadeDto).build();

	}

}
