package test.example.coffeemachineservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RecipeException extends RuntimeException {

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public RecipeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public RecipeException(String message) {
        super(message);
    }
}
