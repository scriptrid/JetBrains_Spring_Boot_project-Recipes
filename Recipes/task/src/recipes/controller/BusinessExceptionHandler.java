package recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import recipes.exceptions.RecipeNotFoundException;
import recipes.exceptions.UserAlreadyExistsException;
import recipes.exceptions.WrongUserException;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Void> onUserExists() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Void> onRecipeNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(WrongUserException.class)
    public ResponseEntity<Void> orWrongUser() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
