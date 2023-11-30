package eu.glasskube.yamlconfigurationgenerator.dtos

import eu.glasskube.yamlconfigurationgenerator.models.Metadata
import eu.glasskube.yamlconfigurationgenerator.models.Spec

data class ConfigurationRequest(val metadata: Metadata, val spec: Spec)
