package ua.shpp.eqbot.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProviderServiceIntegrationTest {

    @Autowired
    ProviderRepository providerRepository;

    ProviderService providerService;
    ProviderEntity entity;


    @Before
    public void setUp() {
        providerService = new ProviderService(providerRepository);

        entity = new ProviderEntity();
        entity.setProviderCity("Dnipro");
        entity.setTelegramId(12L);
        entity.setName("Sashko");

        providerService.saveEntity(entity);
    }

    @After
    public void tearDown() {
        providerService.remove(12L);
    }

    @Test
    public void contextLoads() {
        ProviderEntity dnipro = providerService.getByTelegramIdEntity(12L);
        assertThat(dnipro, is(entity));
    }

}
