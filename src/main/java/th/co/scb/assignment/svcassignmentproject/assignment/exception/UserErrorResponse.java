package th.co.scb.assignment.svcassignmentproject.assignment.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    public UserErrorResponse() {
    }

    public UserErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

}
