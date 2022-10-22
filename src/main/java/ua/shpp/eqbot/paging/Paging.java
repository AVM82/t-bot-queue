package ua.shpp.eqbot.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.repository.ServiceRepository;

import java.util.List;

@Component
public class Paging {
    /*Delete me*/
    private final ServiceRepository serviceRepository;

    public Paging(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * I will give what you send me here, that is, from which record size which
     */
    public List<ServiceEntity> getPage(int from, int size, String likeString) {
        Pageable firstPageWithTwoElements = PageRequest.of(from, size);
        Page<ServiceEntity> byDescriptionLike =
                serviceRepository.findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(likeString, likeString, firstPageWithTwoElements);
        return byDescriptionLike.getContent();
    }
}
