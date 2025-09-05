package co.com.tecnohalecatez.r2dbc;

import co.com.tecnohalecatez.r2dbc.entity.RoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface RoleReactiveRepository
        extends ReactiveCrudRepository<RoleEntity, Integer>, ReactiveQueryByExampleExecutor<RoleEntity> {

}
