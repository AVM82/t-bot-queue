/*
package ua.shpp.eqbot.service;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.telegram.telegrambots.starter.TelegramBotInitializer;
import ua.shpp.eqbot.command.AddService;
import ua.shpp.eqbot.command.CommandContainer;
import ua.shpp.eqbot.command.RegistrationNewUser;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProvideRepository;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

    private ProviderService providerService;


    @BeforeEach
    void setUp() {
        providerService = new ProviderService(provideRepository);
    }

    @DisplayName("2")
    @Test
    void whenPutProviderThanGetProviderUseId() {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setCity("Dnipro");
        providerEntity.setIdTelegram(1L);
        providerEntity.setName("provider");
        providerService.save(providerEntity);
        Optional<ProviderEntity> providerEntity1 = providerService.get(1L);
        ProviderEntity result = new ProviderEntity();
        if (providerEntity1.isPresent()) {
            result = providerEntity1.get();
        }
        assertThat(result.getName(), is("provider"));
    }

    @DisplayName("3")
    @Test
    void whenPutThreeProvidersThenGetProvidersUseIdTelegramExpectedAllList() {
        ProviderEntity providerEntity = new ProviderEntity()
                .setCity("Dnipro");
        ProviderEntity providerEntity1 = new ProviderEntity()
                .setCity("Kiev");
        ProviderEntity providerEntity2 = new ProviderEntity()
                .setCity("Starcon");
        providerEntity.setIdTelegram(1L);
        providerEntity.setName("providerOne");

        providerEntity1.setIdTelegram(1L);
        providerEntity1.setName("providerTwo");

        providerEntity2.setIdTelegram(1L);
        providerEntity2.setName("providerThree");

        providerService.save(providerEntity);
        providerService.save(providerEntity1);
        providerService.save(providerEntity2);

        List<ProviderEntity> byIdTelegram = providerService.getByIdTelegram(1L);

        assertThat(byIdTelegram.size(), is(5));
        assertThat(byIdTelegram.get(2).getCity(), is("Dnipro"));
        assertThat(byIdTelegram.get(3).getCity(), is("Kiev"));
        assertThat(byIdTelegram.get(4).getCity(), is("Starcon"));
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

        providerService.save(providerEntity);

        Optional<ProviderEntity> providerEntity1 = providerService
                .getByNameAndIdTelegram(ID_PROVIDER, "provider");

        ProviderEntity result = new ProviderEntity();
        if (providerEntity1.isPresent()) {
            result = providerEntity1.get();
        }

        assertThat(result.getIdTelegram(), is(ID_PROVIDER));
        assertThat(result.getCity(), is(CITY_PROVIDER));
    }
}
*/
