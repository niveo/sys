package br.com.ams.sys.config;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
@EnableScheduling
public class RedisConfig {
	public static final String CACHE_CIDADE_KEY = "cidades";
	public static final String CACHE_CLIENTES_KEY = "clientes";
	public static final String CACHE_UNIDADES_KEY = "unidades";
	public static final String CACHE_SEGMENTO_CLIENTES_KEY = "segmento_clientes";
	public static final String CACHE_TABELA_PRECO_KEY = "tabela_preco";
	public static final String CACHE_PRODUTO_KEY = "produtos";
	public static final String CACHE_CONFIG_PESQUISA_CODIGO_KEY = "config_pesquisa_codigo";
	public static final String CACHE_REDE_CLIENTES_KEY = "rede_clientes";
	public static final String CACHE_EMPRESAS_KEY = "empresas";
	public static final String CACHE_USUARIO_EMAIL_KEY = "usuario_email";
	public static final String CACHE_ESTADOS_KEY = "estados";
	public static final String CACHE_EMPRESAS_USUARIO_KEY = "empresas_usuario";
	public static final String CACHE_BAIRRO_KEY = "bairros";
	public static final String CACHE_CEP_PESQUISA_KEY = "cep_pesquisa";

	@CacheEvict(allEntries = true, value = { CACHE_ESTADOS_KEY, CACHE_BAIRRO_KEY, CACHE_CIDADE_KEY,
			CACHE_CEP_PESQUISA_KEY, CACHE_EMPRESAS_USUARIO_KEY, CACHE_SEGMENTO_CLIENTES_KEY, CACHE_REDE_CLIENTES_KEY,
			CACHE_TABELA_PRECO_KEY, CACHE_UNIDADES_KEY ,CACHE_CONFIG_PESQUISA_CODIGO_KEY})
	@Scheduled(fixedDelayString = "${cache.ttl.ms}")
	public void evictCache() {
		log.info("Cache cleared");
	}

	@Bean
	RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues().serializeValuesWith(
				RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

}
