package ua.shpp.eqbot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ua.shpp.eqbot.model.ServiceEntity;

import java.util.List;

public interface ServiceRepositoryPaging extends PagingAndSortingRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(String description, String name, Pageable pageable);
}
