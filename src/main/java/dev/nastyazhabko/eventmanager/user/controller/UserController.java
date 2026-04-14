package dev.nastyazhabko.eventmanager.user.controller;

import dev.nastyazhabko.eventmanager.security.SignInRequest;
import dev.nastyazhabko.eventmanager.security.SignUpRequest;
import dev.nastyazhabko.eventmanager.security.jwt.AuthenticationService;
import dev.nastyazhabko.eventmanager.security.jwt.JwtTokenResponse;
import dev.nastyazhabko.eventmanager.user.service.UserService;
import dev.nastyazhabko.eventmanager.user.dto.User;
import dev.nastyazhabko.eventmanager.user.dto.UserDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService,
                          AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest) {
        log.info("Get request for sigh up: {}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new UserDto(
                        user.id(),
                        user.login(),
                        user.age()
                ));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> loginUser(
            @RequestBody @Valid SignInRequest signInRequest
    ) {
        log.info("Get request for login: {}", signInRequest.login());
        var token = authenticationService.authenticateUser(signInRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id) {
        log.info("Get request for user with id {}", id);
        User user = userService.getUserById(id);

        return new UserDto(
                user.id(),
                user.login(),
                user.age()
        );
    }
}
