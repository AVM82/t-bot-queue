package ua.shpp.eqbot.service;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
//@SpringBootTest()
public class ApplicationTest {
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

}
