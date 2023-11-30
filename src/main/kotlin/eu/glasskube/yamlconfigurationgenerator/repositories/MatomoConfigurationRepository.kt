package eu.glasskube.yamlconfigurationgenerator.repositories

import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.models.Metadata
import org.springframework.data.jpa.repository.JpaRepository

interface MatomoConfigurationRepository : JpaRepository<MatomoConfiguration, Long> {
    fun findAllByOrderById() : List<MatomoConfiguration>
    fun existsByMetadata(metadata: Metadata) : Boolean
    fun findByMetadata(metadata: Metadata) : MatomoConfiguration
}