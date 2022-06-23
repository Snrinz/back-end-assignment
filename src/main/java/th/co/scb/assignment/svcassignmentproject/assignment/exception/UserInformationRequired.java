package th.co.scb.assignment.svcassignmentproject.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserInformationRequired extends RuntimeException{

    public UserInformationRequired(String message) {
        super("Lack of required information: " + message);
    }

    public UserInformationRequired(String message, Throwable cause) {
        super(message, cause);
    }

    public UserInformationRequired(Throwable cause) {
        super(cause);
    }
}
