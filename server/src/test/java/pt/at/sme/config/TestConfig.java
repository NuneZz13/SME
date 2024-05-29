package pt.at.sme.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import pt.at.sme.form.util.KtLocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public ObjectMapper objectMapperWithKt() {

        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(kotlinx.datetime.LocalDate.class, new KtLocalDateSerializer());

        objectMapper.registerModule(module);

        return objectMapper;

    }

}
