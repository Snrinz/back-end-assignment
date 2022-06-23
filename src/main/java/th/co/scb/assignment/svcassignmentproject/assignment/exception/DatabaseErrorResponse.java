package th.co.scb.assignment.svcassignmentproject.assignment.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    public DatabaseErrorResponse() {

    }

    public DatabaseErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
