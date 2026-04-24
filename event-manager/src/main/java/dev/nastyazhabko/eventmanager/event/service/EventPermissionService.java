package dev.nastyazhabko.eventmanager.event.service;

import dev.nastyazhabko.eventmanager.event.dto.Event;
import dev.nastyazhabko.eventcommon.security.User;
import dev.nastyazhabko.eventcommon.security.enums.UserRole;
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
