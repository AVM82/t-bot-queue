package ua.shpp.eqbot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ServiceRepositoryDataJpaTest {

    @Autowired
    private ServiceRepository personRepository;

    @Test
    void shouldReturnAlLastNames() {
        personRepository.saveAndFlush(new ServiceEntity().setName("Masha"));
        personRepository.saveAndFlush(new ServiceEntity().setName("Dasha"));
        personRepository.saveAndFlush(new ServiceEntity().setName("Masha"));

        assertEquals(Set.of("Masha", "Dasha"), personRepository.findAllByName());
    }
}
