package th.co.scb.assignment.svcassignmentproject.assignment.exception;

import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import th.co.scb.assignment.svcassignmentproject.assignment.service.Producer;

@RestControllerAdvice
public class DatabaseRestExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    @ExceptionHandler
    public ResponseEntity<DatabaseErrorResponse> handleException(JDBCConnectionException exc) {
        DatabaseErrorResponse error = new DatabaseErrorResponse();

        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        logger.info(String.format("Error: " + exc.getMessage()));

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
