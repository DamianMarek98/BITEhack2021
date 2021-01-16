package giga.koksy.app.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String login) {
        super("User was not found: " + login);
    }
}
