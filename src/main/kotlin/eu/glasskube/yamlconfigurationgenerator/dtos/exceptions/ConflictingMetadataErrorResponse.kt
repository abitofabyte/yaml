package eu.glasskube.yamlconfigurationgenerator.dtos.exceptions

import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.models.Metadata
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConflictingMetadataException

data class ConflictingMetadataErrorResponse(
    val error: String,
    val metadata: Metadata,
    val conflictingWith: MatomoConfiguration
) {
    constructor(exception: ConflictingMetadataException) : this(
        "Conflicting metadata: Name already exists in namespace!",
        exception.metadata,
        exception.conflictingConfiguration
    )
}
