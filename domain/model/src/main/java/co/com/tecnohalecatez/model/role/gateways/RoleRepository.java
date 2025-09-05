package co.com.tecnohalecatez.model.role.gateways;

import co.com.tecnohalecatez.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {

    Mono<Role> findById(Integer id);

}
