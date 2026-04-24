package dev.nastyazhabko.eventmanager.user.service;

import dev.nastyazhabko.eventmanager.security.SignUpRequest;
import dev.nastyazhabko.eventcommon.security.User;
import dev.nastyazhabko.eventmanager.user.entity.UserEntity;
import dev.nastyazhabko.eventcommon.security.enums.UserRole;
import dev.nastyazhabko.eventmanager.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final static Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;
    private final String userPassword;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${pass.admin}") String adminPassword,
                       @Value("${pass.user}") String userPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
        this.userPassword = userPassword;
    }

    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.findByLogin(signUpRequest.login()).isPresent()) {
            throw new IllegalArgumentException("Login already exists");
        }
        var userToSave = new UserEntity(
                null,
                signUpRequest.login(),
                signUpRequest.age(),
                passwordEncoder.encode(signUpRequest.password()),
                UserRole.ROLE_USER.name()
        );

        var savedUser = userRepository.save(userToSave);
        log.info("Registering user {}", signUpRequest);

        return new User(
                savedUser.getId(),
                savedUser.getLogin(),
                savedUser.getAge(),
                UserRole.valueOf(userToSave.getRole())
        );
    }

    public User getUserByLogin(String login) {
        UserEntity user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User with login " + login + " not found"));
        return new User(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                UserRole.valueOf(user.getRole())
        );
    }

    public User getUserById(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return new User(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                UserRole.valueOf(user.getRole())
        );
    }

    public void createDefaultUsers() {
        if (userRepository.findByLogin("admin").isEmpty()) {
            userRepository.save(new UserEntity(
                    null,
                    "admin",
                    0,
                    passwordEncoder.encode(adminPassword),
                    UserRole.ROLE_ADMIN.name()
            ));
            log.info("Creating default admin user");
        }
        if (userRepository.findByLogin("user").isEmpty()) {
            userRepository.save(new UserEntity(
                    null,
                    "user",
                    0,
                    passwordEncoder.encode(userPassword),
                    UserRole.ROLE_USER.name()
            ));
            log.info("Creating default user");
        }
    }
}
