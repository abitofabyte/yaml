package eu.glasskube.yamlconfigurationgenerator.services.exceptions

import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.models.Metadata

data class ConflictingMetadataException(val metadata: Metadata, val conflictingConfiguration: MatomoConfiguration) : RuntimeException()
