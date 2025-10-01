package psu.edu.GameFlix.Services;

import org.springframework.stereotype.Service;

import psu.edu.GameFlix.Repoitories.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository theRoleRepository) {
        roleRepository = theRoleRepository;
    }
}
