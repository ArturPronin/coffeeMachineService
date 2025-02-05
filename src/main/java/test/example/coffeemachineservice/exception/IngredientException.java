package test.example.coffeemachineservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IngredientException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public IngredientException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public IngredientException(String message) {
        super(message);
    }
}