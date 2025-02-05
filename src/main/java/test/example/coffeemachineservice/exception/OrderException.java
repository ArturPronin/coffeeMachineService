package test.example.coffeemachineservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public OrderException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public OrderException(String message) {
        super(message);
    }
}