package test.example.coffeemachineservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DrinkException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public DrinkException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public DrinkException(String message) {
        super(message);
    }
}