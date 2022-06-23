package th.co.scb.assignment.svcassignmentproject.assignment.exception;

public class UserAlreadyExist extends RuntimeException{

    public UserAlreadyExist(String message) {
        super(message);
    }

    public UserAlreadyExist(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExist(Throwable cause) {
        super(cause);
    }
}
