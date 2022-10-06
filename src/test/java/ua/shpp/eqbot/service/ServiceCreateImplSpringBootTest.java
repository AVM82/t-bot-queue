package ua.shpp.eqbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.starter.TelegramBotInitializer;
import ua.shpp.eqbot.command.AddService;
import ua.shpp.eqbot.dto.ServiceDTO;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.repository.ProviderRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ServiceCreateImplSpringBootTest {
    @MockBean
    EqTelegramBot eqTelegramBot;
    @MockBean
    TelegramBotInitializer telegramBotInitializer;
    @MockBean
    SendBotMessageService sendBotMessageService;
    @MockBean
    ImageService imageService;
    @MockBean
    ProviderRepository providerRepository;
    @MockBean
    BundleLanguage bundleLanguage;
    @Autowired
    UserService userService;
    @Autowired
    private ServiceRepository serviceRepository;

    private AddService addService;

    @BeforeEach
    void init() {
        addService = new AddService(sendBotMessageService, serviceRepository, imageService, providerRepository, userService, bundleLanguage);
        serviceRepository.deleteAll();
    }

    @Test
    void whenAddServicesWithSaneTelegramIdThanCountServicesGrow() {
        String first_service = "First Service";
        String second_service = "Two Service";
        String third_service = "Third Service";

        ServiceDTO serviceDTOOne = new ServiceDTO(1L, first_service, "Some info");
        ServiceDTO serviceDTOTwo = new ServiceDTO(1L, second_service, "Two info");
        ServiceDTO serviceDTOThree = new ServiceDTO(1L, third_service, "Third info");

        final var serviceEntity1 = addService.addService(serviceDTOOne);
        final var serviceEntity2 = addService.addService(serviceDTOTwo);
        final var serviceEntity3 = addService.addService(serviceDTOThree);

        assertEquals(1, serviceEntity1.getTelegramId());
        assertEquals(1, serviceEntity2.getTelegramId());
        assertEquals(1, serviceEntity3.getTelegramId());

        assertEquals(first_service, serviceEntity1.getName());
        assertEquals(second_service, serviceEntity2.getName());
        assertEquals(third_service, serviceEntity3.getName());

        assertThat(serviceRepository.count(), is(3L));
    }
}
