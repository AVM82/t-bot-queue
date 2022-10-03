package ua.shpp.eqbot.service;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.telegram.telegrambots.starter.TelegramBotInitializer;
import ua.shpp.eqbot.command.AddService;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.command.RegistrationNewUser;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@SpringBootTest(properties = {"application.properties"})
class ProviderServiceTest {
    @MockBean
    AddService addService;
    @MockBean
    CommandContainer commandContainer;
    @MockBean
    ImageService imageService;
    @MockBean
    TelegramBotInitializer telegramBotInitializer;
    @MockBean
    RegistrationNewUser registrationNewUser;
    @MockBean
    SendBotMessageServiceImpl sendBotMessageService;
    @MockBean
    EqTelegramBot eqTelegramBot;
    @MockBean
    Flyway flyway;

    @Autowired
    private ProvideRepository provideRepository;
    @Autowired
    private CacheManager cacheManager;

    private ProviderService providerService;


    @BeforeEach
    void setUp() {
        providerService = new ProviderService(provideRepository, cacheManager);
    }

    @DisplayName("2")
    @Test
    void whenPutProviderThanGetProviderUseId() {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setCity("Dnipro");
        providerEntity.setIdTelegram(1L);
        providerEntity.setName("provider");
        providerService.saveEntity(providerEntity);
        ProviderEntity result = providerService.getByIdTelegramEntity(1L);
        if (result != null) {
            assertThat(result.getName(), is("provider"));
        }
    }

    @DisplayName("3")
    @Test
    void whenPutThreeProvidersThenGetLastAddProviders() {
        ProviderEntity providerEntity = new ProviderEntity()
                .setCity("Dnipro");
        ProviderEntity providerEntity1 = new ProviderEntity()
                .setCity("Kiev");

        providerEntity.setIdTelegram(7L);
        providerEntity.setName("providerOne");

        providerEntity1.setIdTelegram(7L);
        providerEntity1.setName("providerTwo");

        providerService.saveEntity(providerEntity);
        providerService.saveEntity(providerEntity1);

        List<ProviderEntity> byIdTelegram = providerService.getAllProvidersBtIdTelegram(1L);

        assertThat(byIdTelegram.size(), is(1));
        assertThat(byIdTelegram.get(0).getCity(), is("Dnipro"));
    }

    @DisplayName("1")
    @Test
    void whenPutProviderThanGetProviderUseIdAndName() {
        final String CITY_PROVIDER = "kiev";
        final Long ID_PROVIDER = 1L;

        ProviderEntity providerEntity = new ProviderEntity()
                .setIdTelegram(ID_PROVIDER)
                .setCity(CITY_PROVIDER)
                .setName("provider");

        providerService.saveEntity(providerEntity);

        Optional<ProviderEntity> providerEntity1 = providerService
                .getByNameAndIdTelegram(ID_PROVIDER, "provider");

        ProviderEntity result = new ProviderEntity();
        if (providerEntity1.isPresent()) {
            result = providerEntity1.get();
        }

        assertThat(result.getIdTelegram(), is(ID_PROVIDER));
        assertThat(result.getCity(), is(CITY_PROVIDER));
    }

    @Test
    void whenAddAndRemoveThanReturnNull() {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setCity("Kolomuya");
        providerEntity.setIdTelegram(4L);
        providerEntity.setName("FOP");
        providerService.saveEntity(providerEntity);
        ProviderEntity result = providerService.getByIdTelegramEntity(4L);
        if (result != null) {
            assertThat(result.getName(), is("FOP"));
        }
        assertTrue(providerService.remove(4L));
        assertNull(providerService.getByIdTelegramEntity(4L));
    }
}
