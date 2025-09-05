package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.model.role.Role;
import co.com.tecnohalecatez.model.role.gateways.RoleRepository;
import co.com.tecnohalecatez.r2dbc.entity.RoleEntity;
import co.com.tecnohalecatez.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<Role, RoleEntity,
        Integer, RoleReactiveRepository> implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity, Role.class));
    }

    @Override
    public Mono<Role> findById(Integer id) {
        log.trace("Start find role by id = {}", id);
        return super.findById(id)
                .doOnSuccess(role -> log.trace("Role found by id = {} -> {}", id, role.toString()))
                .doOnError(e -> log.error("Error finding role by id = {} -> {}", id, e.getMessage(), e));
    }

}
