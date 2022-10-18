package ua.shpp.eqbot.controller.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.dto.ServiceRestDTO;
import ua.shpp.eqbot.service.restservice.RestServiceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/service")
public class RestServiceController {

    private final Logger logger = LoggerFactory.getLogger(RestServiceController.class);
    final RestServiceService service;

    public RestServiceController(RestServiceService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "${operation.summary.service.get_all_services}",
            description = "${operation.desc.service.get_all_services}")
    public List<ServiceEntity> getAllServices() {
        logger.info("Getting all services");
        return service.getAllServices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "${operation.summary.service.get_service}",
            description = "${operation.desc.service.get_service}")
    public ServiceEntity getService(@PathVariable Long id) {
        logger.info("Getting service with id {}", id);
        return service.getService(id);
    }

    @GetMapping("/by_telegram_id/{telegramId}")
    @Operation(summary = "${operation.summary.service.get_service_by_telegram}",
            description = "${operation.desc.service.get_service_by_telegram}")
    public List<ServiceEntity> getServicesByTelegramId(@PathVariable Long telegramId) {
        logger.info("Getting all services with telegramId {}", telegramId);
        return service.getAllServiceByTelegramId(telegramId);
    }

    @PostMapping
    @Operation(summary = "${operation.summary.service.post_service}",
            description = "${operation.desc.service.post_service}")
    public void postService(@RequestBody @Valid ServiceRestDTO serviceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.error(error.getDefaultMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entered data is not valid");
        } else {
            service.postService(serviceDTO);
            logger.info("Adding new service with name '{}'", serviceDTO.getName());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "${operation.summary.service.delete_service}",
            description = "${operation.desc.service.delete_service}")
    public void deleteService(@PathVariable Long id) {
        logger.info("Deleting service with id {}", id);
        service.deleteService(id);
    }
}
