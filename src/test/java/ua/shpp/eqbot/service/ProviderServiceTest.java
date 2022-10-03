package ua.shpp.eqbot.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shpp.eqbot.model.ProviderEntity;
import ua.shpp.eqbot.repository.ProviderRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(MockitoExtension.class)
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;
    @Captor
    private ArgumentCaptor<ProviderEntity> argumentCaptor;
    @InjectMocks
    private ProviderService providerService;


    @DisplayName("2")
    @Test
    void whenPutProviderThanGetProviderUseId() {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setProviderCity("Dnipro");
        providerEntity.setTelegramId(1L);
        providerEntity.setName("provider");

        when(providerRepository.save(any())).thenReturn(providerEntity);

        providerService.save(providerEntity);
        verify(providerRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue(), is(providerEntity));
    }

    @DisplayName("3")
    @Test
    @Disabled
    void whenPutThreeProvidersThenGetProvidersUseTelegramIdExpectedAllList() {
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

        assertThat(byTelegramId.size(), is(5));
        assertThat(byTelegramId.get(2).getProviderCity(), is("Dnipro"));
        assertThat(byTelegramId.get(3).getProviderCity(), is("Kiev"));
        assertThat(byTelegramId.get(4).getProviderCity(), is("Starcon"));
    }

    @DisplayName("1")
    @Test
    @Disabled
    void whenPutProviderThanGetProviderUseIdAndName() {
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
