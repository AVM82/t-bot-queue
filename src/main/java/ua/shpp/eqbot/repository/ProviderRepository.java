package ua.shpp.eqbot.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import ua.shpp.eqbot.entity.UserDto;

@Repository
public interface ProviderRepository extends CrudRepository <UserDto, Integer> {

}
