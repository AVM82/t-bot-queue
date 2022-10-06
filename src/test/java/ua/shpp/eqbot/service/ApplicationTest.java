package ua.shpp.eqbot.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

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

        providerService.saveEntity(entity);
    }

    @After
    public void tearDown() {
        providerService.remove(12L);
    }

    @Test
    public void contextLoads() {

        ProviderEntity dnipro = providerService.getByTelegramIdEntity(12L);
        //assertThat(dnipro.isPresent(), Is.is(true));
        assertThat(dnipro.getProviderCity(), Boolean.parseBoolean("Dnipro"));

    }

}
