package eu.glasskube.yamlconfigurationgenerator.endpoints

import eu.glasskube.yamlconfigurationgenerator.dtos.ConfigurationRequest
import eu.glasskube.yamlconfigurationgenerator.dtos.ConfigurationResponse
import eu.glasskube.yamlconfigurationgenerator.dtos.exceptions.ConfigurationNotFoundErrorResponse
import eu.glasskube.yamlconfigurationgenerator.dtos.exceptions.ConflictingMetadataErrorResponse
import eu.glasskube.yamlconfigurationgenerator.models.MatomoConfiguration
import eu.glasskube.yamlconfigurationgenerator.services.MatomoConfigurationService
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConfigurationNotFoundException
import eu.glasskube.yamlconfigurationgenerator.services.exceptions.ConflictingMetadataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/configuration")
class MatomoConfigurationEndpoint(val matomoConfigurationService: MatomoConfigurationService) {

    @GetMapping
    fun getAll(): List<ConfigurationResponse> = matomoConfigurationService.getAllAsResponse()

    @GetMapping("list", produces = ["application/json"])
    fun getList(): List<MatomoConfiguration> = matomoConfigurationService.getAllAsConfiguration()

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long): ConfigurationResponse = matomoConfigurationService.getById(id)

    @PostMapping
    fun create(@RequestBody configurationRequest: ConfigurationRequest) =
        matomoConfigurationService.create(configurationRequest)

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody configurationRequest: ConfigurationRequest): ConfigurationResponse =
        matomoConfigurationService.update(id, configurationRequest)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = matomoConfigurationService.delete(id)

    @ExceptionHandler(ConflictingMetadataException::class)
    fun handleNameAlreadyExistsInNamespaceException(exception: ConflictingMetadataException): ResponseEntity<ConflictingMetadataErrorResponse> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ConflictingMetadataErrorResponse(exception))

    @ExceptionHandler(ConfigurationNotFoundException::class)
    fun handleConfigurationNotFoundException(exception: ConfigurationNotFoundException): ResponseEntity<ConfigurationNotFoundErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConfigurationNotFoundErrorResponse(exception))
}