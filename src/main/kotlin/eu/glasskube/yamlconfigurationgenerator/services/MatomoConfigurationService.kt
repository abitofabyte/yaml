package eu.glasskube.yamlconfigurationgenerator.services

import eu.glasskube.yamlconfigurationgenerator.dtos.ConfigurationRequest
import eu.glasskube.yamlconfigurationgenerator.dtos.ConfigurationResponse
import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.repositories.MatomoConfigurationRepository
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConfigurationNotFoundException
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConflictingMetadataException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Service

@Service
@PropertySource("classpath:application.properties")
class MatomoConfigurationService(
    val matomoConfigurationRepository: MatomoConfigurationRepository,
    @Value("\${yaml.apiVersion}") val apiVersion: String,
    @Value("\${yaml.kind}") val kind: String
) {

    fun getAllAsResponse(): List<ConfigurationResponse> = matomoConfigurationRepository.findAllByOrderById()
        .map { config -> ConfigurationResponse(config.apiVersion, config.kind, config.metadata, config.spec) }

    fun getAllAsConfiguration() : List<MatomoConfiguration> = matomoConfigurationRepository.findAllByOrderById()

    fun getById(id: Long): ConfigurationResponse = ConfigurationResponse(matomoConfigurationRepository.findById(id).orElseThrow { ConfigurationNotFoundException(id) })

    @Transactional
    fun create(configurationRequest: ConfigurationRequest): ConfigurationResponse {
        if (matomoConfigurationRepository.existsByMetadata(configurationRequest.metadata)) {
            val conflicting = matomoConfigurationRepository.findByMetadata(configurationRequest.metadata)
            throw ConflictingMetadataException(
                configurationRequest.metadata,
                conflicting
            )
        }
        val configuration = matomoConfigurationRepository.save(
            MatomoConfiguration(
                0,
                apiVersion,
                kind,
                configurationRequest.metadata,
                configurationRequest.spec
            )
        )
        return ConfigurationResponse(configuration)
    }

    @Transactional
    fun update(id: Long, configurationRequest: ConfigurationRequest): ConfigurationResponse {
        if(!matomoConfigurationRepository.existsById(id)) {
            throw ConfigurationNotFoundException(id)
        }
        if (matomoConfigurationRepository.existsByMetadata(configurationRequest.metadata)) {
            val conflicting = matomoConfigurationRepository.findByMetadata(configurationRequest.metadata)
            throw ConflictingMetadataException(
                configurationRequest.metadata,
                conflicting
            )
        }
        val configuration = matomoConfigurationRepository.save(
            MatomoConfiguration(
                id,
                apiVersion,
                kind,
                configurationRequest.metadata,
                configurationRequest.spec
            )
        )
        return ConfigurationResponse(configuration)
    }

    @Transactional
    fun delete(id: Long) {
        if(!matomoConfigurationRepository.existsById(id)) {
            throw ConfigurationNotFoundException(id)
        }
        matomoConfigurationRepository.deleteById(id)
    }


}