package eu.glasskube.yamlconfigurationgenerator.dtos.exceptions

import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConfigurationNotFoundException

data class ConfigurationNotFoundErrorResponse(val error: String, val notFound: Long) {
    constructor(exception: ConfigurationNotFoundException) : this("Configuration not found!", exception.id)
}