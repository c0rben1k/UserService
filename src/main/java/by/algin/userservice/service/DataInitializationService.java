package by.algin.userservice.service;

import by.algin.constants.CommonRoleConstants;
import by.algin.userservice.config.AppProperties;
import by.algin.userservice.entity.Role;
import by.algin.userservice.entity.User;
import by.algin.userservice.repository.RoleRepository;
import by.algin.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataInitializationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeDefaultRoles() {
        log.info("Initializing default roles...");
        
        try {
            if (!roleRepository.existsByName(CommonRoleConstants.ADMIN)) {
                Role adminRole = new Role();
                adminRole.setName(CommonRoleConstants.ADMIN);
                roleRepository.save(adminRole);
                log.info("Created {}", CommonRoleConstants.ADMIN);
            } else {
                log.info("{} already exists", CommonRoleConstants.ADMIN);
            }

            if (!roleRepository.existsByName(CommonRoleConstants.USER)) {
                Role userRole = new Role();
                userRole.setName(CommonRoleConstants.USER);
                roleRepository.save(userRole);
                log.info("Created {}", CommonRoleConstants.USER);
            } else {
                log.info("{} already exists", CommonRoleConstants.USER);
            }

            log.info("Default roles initialization completed");

            createDefaultAdmin();

        } catch (Exception e) {
            log.error("Failed to initialize default roles: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void reinitializeRoles() {
        log.info("Reinitializing roles...");
        
        try {
            if (!roleRepository.existsByName(CommonRoleConstants.ADMIN)) {
                Role adminRole = new Role();
                adminRole.setName(CommonRoleConstants.ADMIN);
                roleRepository.save(adminRole);
                log.info("Created {}", CommonRoleConstants.ADMIN);
            }

            if (!roleRepository.existsByName(CommonRoleConstants.USER)) {
                Role userRole = new Role();
                userRole.setName(CommonRoleConstants.USER);
                roleRepository.save(userRole);
                log.info("Created {}", CommonRoleConstants.USER);
            }

            log.info("Roles reinitialization completed");
            
        } catch (Exception e) {
            log.error("Failed to reinitialize roles: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to reinitialize roles", e);
        }
    }

    private void createDefaultAdmin() {
        log.info("Checking for default admin user...");

        try {
            AppProperties.DefaultAdmin adminConfig = appProperties.getDefaultAdmin();

            if (userRepository.existsByUsername(adminConfig.getUsername())) {
                log.info("Admin user already exists: {}", adminConfig.getUsername());
                return;
            }

            Role adminRole = roleRepository.findByName(CommonRoleConstants.ADMIN)
                    .orElseThrow(() -> new RuntimeException(CommonRoleConstants.ADMIN + " not found"));

            User adminUser = new User();
            adminUser.setUsername(adminConfig.getUsername());
            adminUser.setEmail(adminConfig.getEmail());
            adminUser.setPassword(passwordEncoder.encode(adminConfig.getPassword()));
            adminUser.setRoles(Set.of(adminRole));
            adminUser.setEnabled(true);
            adminUser.setConfirmationToken(null);
            adminUser.setCreatedAt(LocalDateTime.now());

            userRepository.save(adminUser);
            log.info("Created default admin user with username: {}", adminConfig.getUsername());

        } catch (Exception e) {
            log.error("Failed to create default admin user: {}", e.getMessage(), e);
        }
    }
}
