package eu.glasskube.yamlconfigurationgenerator.dtos

import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.models.Metadata
import eu.glasskube.yamlconfigurationgenerator.models.Spec

data class ConfigurationResponse(
    val apiVersion: String,
    val kind: String,
    val metadata: Metadata,
    val spec: Spec
) {
    constructor(matomoConfiguration: MatomoConfiguration) : this(
        matomoConfiguration.apiVersion,
        matomoConfiguration.kind,
        matomoConfiguration.metadata,
        matomoConfiguration.spec
    )
}
