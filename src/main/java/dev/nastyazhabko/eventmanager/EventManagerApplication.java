package dev.nastyazhabko.eventmanager;

import dev.nastyazhabko.eventmanager.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventManagerApplication.class, args);
    }
}
