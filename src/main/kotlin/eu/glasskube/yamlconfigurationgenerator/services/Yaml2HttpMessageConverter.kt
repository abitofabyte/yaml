package eu.glasskube.yamlconfigurationgenerator.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter

class Yaml2HttpMessageConverter(objectMapper: ObjectMapper) : AbstractJackson2HttpMessageConverter(objectMapper, MediaType.parseMediaType("application/yaml"))