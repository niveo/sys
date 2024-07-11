package br.com.ams.sys.config;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.ams.sys.common.IgnoreHibernatePropertiesInJackson;
import br.com.ams.sys.serializer.MillisOrLocalDateDeserializer;
import br.com.ams.sys.serializer.MillisOrLocalDateSerializer;
import br.com.ams.sys.serializer.MillisOrLocalDateTimeDeserializer;
import br.com.ams.sys.serializer.MillisOrLocalDateTimeSerializer;

@Configuration
public class JacksonAutoConfigurationDate {
	@Bean
	Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		return builder -> {
			builder.timeZone(ZoneId.systemDefault().getId());
			builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
			builder.deserializerByType(ZonedDateTime.class, new MillisOrLocalDateTimeDeserializer())
					.serializerByType(ZonedDateTime.class, new MillisOrLocalDateTimeSerializer())

					.serializerByType(LocalDate.class, new MillisOrLocalDateSerializer())
					.deserializerByType(LocalDate.class, new MillisOrLocalDateDeserializer());

		};
	}

	@Bean
	ObjectMapper objectMapper() {

		System.out.println("JacksonConfiguration-objectMapper");

		var mapper = new ObjectMapper();

		var hi = new Hibernate6Module();
		hi.disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
		hi.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);

		mapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);

		// Não travar os filtros usados nas classes, principalmente a DashBoardService
		mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));

		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));

		mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
		mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);

		var simpleModule = new SimpleModule();
		// simpleModule.addSerializer(Parceiro.class, new ParceiroSerializer());
		// simpleModule.addSerializer(Vendedor.class, new VendedorSerializer());

		simpleModule.addSerializer(LocalDate.class, new MillisOrLocalDateSerializer());
		simpleModule.addDeserializer(LocalDate.class, new MillisOrLocalDateDeserializer());

		simpleModule.addSerializer(ZonedDateTime.class, new MillisOrLocalDateTimeSerializer());
		simpleModule.addDeserializer(ZonedDateTime.class, new MillisOrLocalDateTimeDeserializer());

		mapper.registerModule(new JavaTimeModule());
		//mapper.registerModule(simpleModule);
		mapper.registerModule(hi);

		return mapper;
	}

}
