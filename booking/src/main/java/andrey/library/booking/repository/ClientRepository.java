package andrey.library.booking.repository;

import andrey.library.booking.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByAccount(String account);
}
