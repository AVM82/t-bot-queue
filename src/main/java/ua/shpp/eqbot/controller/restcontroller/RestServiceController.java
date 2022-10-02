package ua.shpp.eqbot.controller.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.shpp.eqbot.model.ServiceDTO;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.ServiceRestDTO;
import ua.shpp.eqbot.service.restservice.RestServiceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/service")
public class RestServiceController {

    Logger log = LoggerFactory.getLogger(RestServiceController.class);
    @Autowired
    RestServiceService service;

    @GetMapping
    @Operation(summary = "${operation.summary.service.get_all_services}",
            description = "${operation.desc.service.get_all_services}")
    public List<ServiceEntity> getAllServices(){
        log.info("Getting all services");
        return service.getAllServices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "${operation.summary.service.get_service}",
            description = "${operation.desc.service.get_service}")
    public ServiceEntity getService(@PathVariable Long id){
        log.info("Getting service with id {}", id);
        return service.getService(id);
    }

    @GetMapping("/by_telegram_id/{idTelegram}")
    @Operation(summary = "${operation.summary.service.get_service_by_telegram}",
            description = "${operation.desc.service.get_service_by_telegram}")
    public List<ServiceEntity> getServicesByIdTelegram(@PathVariable Long idTelegram){
        log.info("Getting all services with idTelegram {}", idTelegram);
        return service.getAllServiceByIdTelegram(idTelegram);
    }

    @PostMapping
    @Operation(summary = "${operation.summary.service.post_service}",
            description = "${operation.desc.service.post_service}")
    public void postService(@RequestBody @Valid ServiceRestDTO serviceDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                log.error(error.getDefaultMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entered data is not valid" );
        } else {
            service.postService(serviceDTO);
            log.info("Adding new service with name '{}'", serviceDTO.getName());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "${operation.summary.service.delete_service}",
            description = "${operation.desc.service.delete_service}")
    public void deleteService(@PathVariable Long id){
        log.info("Deleting service with id {}", id);
        service.deleteService(id);
    }

}
