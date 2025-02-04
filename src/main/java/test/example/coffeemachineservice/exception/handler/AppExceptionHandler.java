package test.example.coffeemachineservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.example.coffeemachineservice.exception.DrinkException;
import test.example.coffeemachineservice.exception.IngredientException;
import test.example.coffeemachineservice.exception.OrderException;
import test.example.coffeemachineservice.exception.RecipeException;

import static test.example.coffeemachineservice.constant.ApplicationConstant.ERROR_LOG_TEMPLATE;


@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(RecipeException.class)
    public ResponseEntity<String> handleRecipeException(RecipeException exception) {
        log.error(ERROR_LOG_TEMPLATE, exception.getStatus(), exception.getMessage(), exception.getStackTrace());
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(IngredientException.class)
    public ResponseEntity<String> handleIngredientException(IngredientException exception) {
        log.error(ERROR_LOG_TEMPLATE, exception.getStatus(), exception.getMessage(), exception.getStackTrace());
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(DrinkException.class)
    public ResponseEntity<String> handleDrinkException(DrinkException exception) {
        log.error(ERROR_LOG_TEMPLATE, exception.getStatus(), exception.getMessage(), exception.getStackTrace());
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> handleOrderException(OrderException exception) {
        log.error(ERROR_LOG_TEMPLATE, exception.getStatus(), exception.getMessage(), exception.getStackTrace());
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getMessage());
    }
}