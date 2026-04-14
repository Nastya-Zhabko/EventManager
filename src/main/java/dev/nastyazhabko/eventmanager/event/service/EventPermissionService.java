package dev.nastyazhabko.eventmanager.event.service;

import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventmanager.user.dto.User;
import dev.nastyazhabko.eventmanager.user.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EventPermissionService {
    public boolean canModifyEvent(
            User user,
            Event event) {
        return Objects.equals(event.ownerId(), user.id()) || user.role() == UserRole.ROLE_ADMIN;
    }
}
