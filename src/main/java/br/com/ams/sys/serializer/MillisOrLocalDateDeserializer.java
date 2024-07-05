package br.com.ams.sys.serializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@JsonComponent
public class MillisOrLocalDateDeserializer extends JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
		try {
			return LocalDate.ofInstant(Instant.ofEpochMilli(Long.parseLong(p.getText())),
					TimeZone.getTimeZone((ZoneId.systemDefault())).toZoneId());
		} catch (DateTimeParseException e) {
			System.err.println(e);
			return null;
		}
	}

}