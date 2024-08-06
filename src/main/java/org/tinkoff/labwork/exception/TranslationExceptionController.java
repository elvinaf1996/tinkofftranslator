package org.tinkoff.labwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TranslationExceptionController {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalStateAndIllegalArgumentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TranslatorException.class)
    protected ResponseEntity<String> handleTranslatorExceptionException(TranslatorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка доступа к ресурсу перевода");
    }

    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<String> handleDatabaseExceptionException(DatabaseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка доступа к базе данных");
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
