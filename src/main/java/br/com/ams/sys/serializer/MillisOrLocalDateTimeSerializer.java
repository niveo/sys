package br.com.ams.sys.serializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class MillisOrLocalDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

	@Override
	public void serialize(ZonedDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
		try {
			g.writeNumber(value.toInstant().toEpochMilli());
		} catch (DateTimeParseException e) {
			System.err.println(e);
			g.writeString("");
		}
	}

}
