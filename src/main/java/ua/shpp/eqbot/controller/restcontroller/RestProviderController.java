package ua.shpp.eqbot.controller.restcontroller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ua.shpp.eqbot.model.ProviderDto;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.service.restservice.RestProviderService;

import javax.validation.Valid;
import java.util.List;


@RestController()
@RequestMapping("/provider")
@PropertySource("classpath:language.properties")
public class RestProviderController {


    Logger log = LoggerFactory.getLogger(RestProviderController.class);
    @Autowired
    RestProviderService service;

    @GetMapping
    @Operation(summary = "${operation.summary.provider.get_all_providers}",
            description = "${operation.desc.provider.get_all_providers}")
    public List<ProviderEntity> getAllProviders(){
        log.info("Getting all providers");
        return service.getAllProviders();
    }


    @GetMapping("/{idTelegram}")
    @Operation(summary = "${operation.summary.provider.get_provider}",
            description = "${operation.desc.provider.get_provider}")
    public ProviderEntity getProvider(@PathVariable Long idTelegram){
        log.info("Geting provider with Telegram id {}", idTelegram);
        return service.getProvider(idTelegram);

    }

    @PostMapping
    @Operation(summary = "${operation.summary.provider.post_provider}",
            description = "${operation.desc.provider.post_provider}")
    public void postProvider(@RequestBody @Valid ProviderDto providerDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                log.error(error.getDefaultMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entered data is not valid" );
        } else {
            service.postProvider(providerDto);
            log.info("Adding new provider with name '{}'", providerDto.getName());
        }

    }

    @DeleteMapping("/{idTelegram}")
    @Operation(summary = "${operation.summary.provider.delete_provider}",
            description = "${operation.desc.provider.delete_provider}")
    public void deleteProvider(@PathVariable Long idTelegram){
        service.deleteProvider(idTelegram);
        log.info("Deleting provider with Telegram id [" +idTelegram +"]");
    }
}
