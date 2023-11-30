package eu.glasskube.yamlconfigurationgenerator.configuration

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import eu.glasskube.yamlconfigurationgenerator.services.Yaml2HttpMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonYamlConfiguration {
    @Bean
    fun yaml2HttpMessageConverter() : Yaml2HttpMessageConverter {
        val mapper = YAMLMapper()
        mapper.enable(SerializationFeature.INDENT_OUTPUT)

        return Yaml2HttpMessageConverter(mapper)
    }
}