package br.com.ams.sys.serializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class MillisOrLocalDateSerializer extends JsonSerializer<LocalDate> {

	@Override
	public void serialize(LocalDate value, JsonGenerator g, SerializerProvider provider) throws IOException {
		try {
			g.writeNumber(value.atStartOfDay(TimeZone.getTimeZone((ZoneId.systemDefault())).toZoneId()).toInstant()
					.toEpochMilli());
		} catch (DateTimeParseException e) {
			System.err.println(e);
			g.writeString("");
		}
	}

}
