package pt.at.sme.form.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * JSON serializer rule when working with Kotlin {@link kotlinx.datetime.LocalDate}
 */
public class KtLocalDateSerializer extends JsonSerializer<kotlinx.datetime.LocalDate> {

    /**
     * The actual serialization from {@link kotlinx.datetime.LocalDate} to a value in the JSON.
     *
     * @param value       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     *                    serializing Objects value contains, if any.
     * @throws IOException -
     */
    @Override
    public void serialize(kotlinx.datetime.LocalDate value,
                          JsonGenerator gen,
                          SerializerProvider serializers) throws IOException {

        gen.writeString(value.toString());
    }

}
