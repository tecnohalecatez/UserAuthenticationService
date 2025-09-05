package co.com.tecnohalecatez.usecase.role;

import co.com.tecnohalecatez.model.role.Role;
import co.com.tecnohalecatez.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleRepository roleRepository;

    public Mono<Role> getRoleById(Integer id) {
        return roleRepository.findById(id);
    }

}
