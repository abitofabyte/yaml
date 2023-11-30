package eu.glasskube.yamlconfigurationgenerator.models

import jakarta.persistence.Embeddable

@Embeddable
data class Spec(val host: String)
