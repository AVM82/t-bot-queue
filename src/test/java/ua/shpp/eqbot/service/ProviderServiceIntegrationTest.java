package ua.shpp.eqbot.service;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProviderServiceIntegrationTest {

    @Autowired
    ProviderRepository providerRepository;

    ProviderService providerService;


    @Before
    public void setUp() {
        providerService = new ProviderService(providerRepository);

        ProviderEntity entity = new ProviderEntity();
        entity.setProviderCity("Dnipro");
        entity.setTelegramId(12L);

        providerService.save(entity);
    }

    @After
    public void tearDown() {
        providerService.remove(12L);
    }

    @Test
    public void contextLoads() {

        Optional<ProviderEntity> dnipro = providerService.getByNameAndTelegramId(12L, "Dnipro");
        assertThat(dnipro.isPresent(), Is.is(true));
    }


    @Test
    public void whenPutThreeProvidersThenGetProvidersUseTelegramIdExpectedAllList() {
        ProviderEntity providerEntity = new ProviderEntity()
                .setProviderCity("Dnipro");
        ProviderEntity providerEntity1 = new ProviderEntity()
                .setProviderCity("Kiev");
        ProviderEntity providerEntity2 = new ProviderEntity()
                .setProviderCity("Starcon");
        providerEntity.setTelegramId(1L);
        providerEntity.setName("providerOne");

        providerEntity1.setTelegramId(1L);
        providerEntity1.setName("providerTwo");

        providerEntity2.setTelegramId(1L);
        providerEntity2.setName("providerThree");

        providerService.save(providerEntity);
        providerService.save(providerEntity1);
        providerService.save(providerEntity2);

        List<ProviderEntity> byTelegramId = providerService.getByTelegramId(1L);

        assertThat(byTelegramId.size(), is(3));
        assertThat(byTelegramId.get(0).getProviderCity(), is("Dnipro"));
        assertThat(byTelegramId.get(1).getProviderCity(), is("Kiev"));
        assertThat(byTelegramId.get(2).getProviderCity(), is("Starcon"));
    }

    @Test
    public void whenPutProviderThanGetProviderUseIdAndName() {
        final String cityProvider = "kiev";
        final Long providerId = 1L;

        ProviderEntity providerEntity = new ProviderEntity()
                .setTelegramId(providerId)
                .setProviderCity(cityProvider);

        providerService.save(providerEntity);

        Optional<ProviderEntity> providerEntity1 = providerService
                .getByNameAndTelegramId(providerId, cityProvider);

        ProviderEntity result = new ProviderEntity();
        if (providerEntity1.isPresent()) {
            result = providerEntity1.get();
        }

        assertThat(result.getTelegramId(), is(providerId));

        assertThat(result.getProviderCity(), is(cityProvider));
    }

}
