package eu.glasskube.yamlconfigurationgenerator.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class MatomoConfiguration(
    @Id
    @GeneratedValue
    val id: Long,

    val apiVersion: String,

    val kind: String,

    val metadata: Metadata,

    val spec: Spec
)