package ua.shpp.eqbot.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;

import java.util.List;

@Component
public class Paginator {
    private final ServiceRepository serviceRepository;

    public Paginator(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * я дам те що ти мені пришлел сюди тобто від якого запису по який
     */
    public List<ServiceEntity> getPage2(int from, int to) {
        Pageable firstPageWithTwoElements = PageRequest.of(from, to);
        Page<ServiceEntity> pagingTwoEntity = serviceRepository.findAll(firstPageWithTwoElements);
        System.out.println("=====================2 version================");
        return pagingTwoEntity.getContent();
    }
}
