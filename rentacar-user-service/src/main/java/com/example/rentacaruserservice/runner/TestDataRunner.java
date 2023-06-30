package com.example.rentacaruserservice.runner;

import com.example.rentacaruserservice.domain.ClientStatus;
import com.example.rentacaruserservice.domain.Manager;
import com.example.rentacaruserservice.domain.Role;
import com.example.rentacaruserservice.domain.User;
import com.example.rentacaruserservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Profile({"default"})
@Component
public class TestDataRunner implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private ClientStatusRepository clientStatusRepository;
    private ManagerRepository managerRepository;


    public TestDataRunner(RoleRepository roleRepository, UserRepository userRepository, ClientRepository clientRepository,
                          ManagerRepository managerRepository, ClientStatusRepository clientStatusRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.managerRepository = managerRepository;
        this.clientStatusRepository = clientStatusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Insert roles
        Role roleUser = new Role("ROLE_CLIENT", "Client role");
        Role roleManager = new Role("ROLE_MANAGER", "Manager role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");

        roleRepository.save(roleUser);
        roleRepository.save(roleManager);
        roleRepository.save(roleAdmin);
        //Insert admin
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setBanned(false);
        admin.setRole(roleAdmin);

        Manager manager = new Manager();
        manager.setBanned(false);
        manager.setUsername("manager");
        manager.setPassword("manager");
        manager.setEmail("manager@gmail.com");
        manager.setRole(roleManager);
        manager.setBusinessName("biznis1");
        manager.setHiringDate(Date.valueOf(LocalDate.now()));

        managerRepository.save(manager);
        userRepository.save(admin);

        //Insert ClientStatus
        clientStatusRepository.save(new ClientStatus("Regular", 0, 10, 0));
        clientStatusRepository.save(new ClientStatus("Silver", 11, 4, 5));
        clientStatusRepository.save(new ClientStatus("Gold", 14, 21, 10));
        clientStatusRepository.save(new ClientStatus("Platinum", 21, 100,  15));

    }
}