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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderServiceUnitTest {

    @Mock
    private ProviderRepository providerRepository;
    @Captor
    private ArgumentCaptor<ProviderEntity> argumentCaptor;
    @Captor
    private ArgumentCaptor<Long> captor;
    @InjectMocks
    private ProviderService providerService;


    @Test
    void whenSaveIntoProviderServiceThanSameEntityUseInProviderRepository() {
        ProviderEntity providerEntity = new ProviderEntity();
        providerEntity.setProviderCity("Dnipro");
        providerEntity.setTelegramId(1L);
        providerEntity.setName("provider");

        when(providerRepository.save(any())).thenReturn(providerEntity);

        providerService.saveEntity(providerEntity);
        verify(providerRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue(), is(providerEntity));
    }

    @Test
    void whenGetByTelegramIdIntoProviderServiceThanSameEntityUseInProviderRepository() {
//        List<ProviderEntity> entityList = List.of(new ProviderEntity());
        ProviderEntity entity = new ProviderEntity();

        when(providerRepository.findByTelegramId(any())).thenReturn(entity);

        providerService.getByTelegramIdEntity(17L);
        verify(providerRepository).findByTelegramId(captor.capture());

        assertThat(captor.getValue(), is(17L));
    }

}
