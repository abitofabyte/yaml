package eu.glasskube.yamlconfigurationgenerator.models

import jakarta.persistence.Embeddable

@Embeddable
data class Metadata(
    val name: String,
    val namespace: String
)
