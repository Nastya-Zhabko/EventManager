package dev.nastyazhabko.eventmanager.event.exception;

public class UserNotAdminAndNotOwnerOfEventException extends RuntimeException {
    public UserNotAdminAndNotOwnerOfEventException(String message) {
        super(message);
    }
}
