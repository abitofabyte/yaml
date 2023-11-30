package eu.glasskube.yamlconfigurationgenerator.services

import eu.glasskube.yamlconfigurationgenerator.dtos.ConfigurationRequest
import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.models.Metadata
import eu.glasskube.yamlconfigurationgenerator.models.Spec
import eu.glasskube.yamlconfigurationgenerator.repositories.MatomoConfigurationRepository
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConfigurationNotFoundException
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConflictingMetadataException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.util.*

class MatomoConfigurationServiceTest {
    private val apiVersion = "apiVersion"
    private val kind = "kind"
    private lateinit var matomoConfigurationRepository: MatomoConfigurationRepository
    private lateinit var matomoConfigurationService: MatomoConfigurationService
    private lateinit var metadata: Metadata
    private lateinit var spec: Spec

    @BeforeEach
    fun setUp() {
        matomoConfigurationRepository = mock(MatomoConfigurationRepository::class.java)
        matomoConfigurationService = MatomoConfigurationService(matomoConfigurationRepository, apiVersion, kind)
        metadata = mock(Metadata::class.java)
        spec = mock(Spec::class.java)
    }


    @Test
    fun getAllAsResponse() {
        val response = listOf(MatomoConfiguration(1, apiVersion, kind, metadata, spec))
        `when`(matomoConfigurationRepository.findAllByOrderById()).thenReturn(response)

        val result = matomoConfigurationService.getAllAsResponse()

        assert(result.size == 1)

        val firstResult = result[0]

        assert(firstResult.apiVersion == apiVersion)
        assert(firstResult.kind == kind)
        assert(firstResult.metadata == metadata)
        assert(firstResult.spec == spec)

        verify(matomoConfigurationRepository).findAllByOrderById()
    }

    @Test
    fun getAllAsConfiguration() {
        val response = listOf(MatomoConfiguration(1, apiVersion, kind, metadata, spec))
        `when`(matomoConfigurationRepository.findAllByOrderById()).thenReturn(response)

        val result = matomoConfigurationService.getAllAsConfiguration()

        assert(result.size == 1)

        val firstResult = result[0]

        assert(firstResult.id == 1L)
        assert(firstResult.apiVersion == apiVersion)
        assert(firstResult.kind == kind)
        assert(firstResult.metadata == metadata)
        assert(firstResult.spec == spec)

        verify(matomoConfigurationRepository).findAllByOrderById()
    }

    @Test
    fun getById() {
        val id = 1L

        val response = Optional.of(MatomoConfiguration(id, apiVersion, kind, metadata, spec))
        `when`(matomoConfigurationRepository.findById(id)).thenReturn(response)

        val result = matomoConfigurationService.getById(id)

        assert(result.apiVersion == apiVersion)
        assert(result.kind == kind)
        assert(result.metadata == metadata)
        assert(result.spec == spec)

        verify(matomoConfigurationRepository).findById(id)
    }

    @Test
    fun getByIdNotFoundThrowsException() {
        val id = 1L

        `when`(matomoConfigurationRepository.findById(id)).thenReturn(Optional.empty())

        assertThrows<ConfigurationNotFoundException> {
            matomoConfigurationService.getById(id)
        }

        verify(matomoConfigurationRepository).findById(id)
    }

    @Test
    fun create() {
        val configurationRequest = ConfigurationRequest(metadata, spec)

        val savedConfiguration = MatomoConfiguration(0, apiVersion, kind, metadata, spec)

        `when`(matomoConfigurationRepository.existsByMetadata(metadata)).thenReturn(false)
        `when`(matomoConfigurationRepository.save(MatomoConfiguration(0, apiVersion, kind, metadata, spec))).thenReturn(
            savedConfiguration
        )

        val result = matomoConfigurationService.create(configurationRequest)

        assert(result.apiVersion == apiVersion)
        assert(result.kind == kind)
        assert(result.metadata == metadata)
        assert(result.spec == spec)

        verify(matomoConfigurationRepository).existsByMetadata(metadata)
        verify(matomoConfigurationRepository).save(MatomoConfiguration(0, apiVersion, kind, metadata, spec))
    }

    @Test
    fun createDuplicateThrowsException() {
        val configurationRequest = ConfigurationRequest(metadata, spec)

        `when`(matomoConfigurationRepository.existsByMetadata(metadata)).thenReturn(true)
        `when`(matomoConfigurationRepository.findByMetadata(metadata)).thenReturn(
            MatomoConfiguration(
                1,
                apiVersion,
                kind,
                metadata,
                spec
            )
        )

        assertThrows<ConflictingMetadataException> {
            matomoConfigurationService.create(configurationRequest)
        }

        verify(matomoConfigurationRepository).existsByMetadata(metadata)
        verify(matomoConfigurationRepository).findByMetadata(metadata)
    }

    @Test
    fun update() {
        val configurationRequest = ConfigurationRequest(metadata, spec)
        val id = 1L

        `when`(matomoConfigurationRepository.existsById(id)).thenReturn(true)
        `when`(matomoConfigurationRepository.existsByMetadata(metadata)).thenReturn(false)
        `when`(
            matomoConfigurationRepository.save(
                MatomoConfiguration(
                    id,
                    apiVersion,
                    kind,
                    metadata,
                    spec
                )
            )
        ).thenReturn(
            MatomoConfiguration(id, apiVersion, kind, metadata, spec)
        )

        val result = matomoConfigurationService.update(id, configurationRequest)

        assert(result.apiVersion == apiVersion)
        assert(result.kind == kind)
        assert(result.metadata == metadata)
        assert(result.spec == spec)

        verify(matomoConfigurationRepository).existsById(id)
        verify(matomoConfigurationRepository).existsByMetadata(metadata)
        verify(matomoConfigurationRepository).save(MatomoConfiguration(id, apiVersion, kind, metadata, spec))
    }

    @Test
    fun updateDuplicateThrowsException() {
        val configurationRequest = ConfigurationRequest(metadata, spec)
        val id = 1L

        `when`(matomoConfigurationRepository.existsById(id)).thenReturn(true)
        `when`(matomoConfigurationRepository.existsByMetadata(metadata)).thenReturn(true)
        `when`(matomoConfigurationRepository.findByMetadata(metadata)).thenReturn(
            MatomoConfiguration(
                id,
                apiVersion,
                kind,
                metadata,
                spec
            )
        )

        assertThrows<ConflictingMetadataException> {
            matomoConfigurationService.update(id, configurationRequest)
        }

        verify(matomoConfigurationRepository).existsById(id)
        verify(matomoConfigurationRepository).existsByMetadata(metadata)
    }

    @Test
    fun updateIdNotFoundThrowsException() {
        val id = 1L

        `when`(matomoConfigurationRepository.existsById(id)).thenReturn(false)

        assertThrows<ConfigurationNotFoundException> {
            matomoConfigurationService.update(id, ConfigurationRequest(metadata, spec))
        }

        verify(matomoConfigurationRepository).existsById(id)
    }
}