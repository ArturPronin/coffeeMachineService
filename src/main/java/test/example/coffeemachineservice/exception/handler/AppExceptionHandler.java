package test.example.coffeemachineservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
}
