package ua.shpp.eqbot.controller.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.shpp.eqbot.dto.ProviderDto;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.service.restservice.RestProviderService;

import javax.validation.Valid;
import java.util.List;


@RestController()
@RequestMapping("/provider")
@PropertySource("classpath:language_en.properties")
public class RestProviderController {

    private final Logger logger = LoggerFactory.getLogger(RestProviderController.class);
    private final RestProviderService service;

    public RestProviderController(RestProviderService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "${operation.summary.provider.get_all_providers}",
            description = "${operation.desc.provider.get_all_providers}")
    public List<ProviderEntity> getAllProviders() {
        logger.info("Getting all providers");
        return service.getAllProviders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "${operation.summary.provider.get_provider}",
            description = "${operation.desc.provider.get_provider}")
    public ProviderEntity getProvider(@PathVariable Long id) {
        logger.info("Getting provider with id {}", id);
        return service.getProvider(id);

    }

    @PostMapping
    @Operation(summary = "${operation.summary.provider.post_provider}",
            description = "${operation.desc.provider.post_provider}")
    public void postProvider(@RequestBody @Valid ProviderDto providerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.error(error.getDefaultMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entered data is not valid");
        } else {
            service.postProvider(providerDto);
            logger.info("Adding new provider with name '{}'", providerDto.getName());
        }

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "${operation.summary.provider.delete_provider}",
            description = "${operation.desc.provider.delete_provider}")
    public void deleteProvider(@PathVariable Long id) {
        service.deleteProvider(id);
        logger.info("Deleting provider with id [{}]", id);
    }
}
