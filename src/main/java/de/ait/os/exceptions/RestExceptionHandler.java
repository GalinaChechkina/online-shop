package de.ait.os.exceptions;

import de.ait.os.dto.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 6/29/2024
 * education-center
 *
 * @author Chechkina (AIT TR)
 */

@ControllerAdvice //отслеживаем, что происходит в контроллере и реагируем на это
public class RestExceptionHandler { //обработчик ошибок

    @ExceptionHandler(value = RestException.class)//метод, кот. б. перехватывать ошибки RestException
    public ResponseEntity<StandardResponseDto> handlerRestException(RestException e){

        return ResponseEntity.status(e.getStatus())
                .body(StandardResponseDto.builder()
                        .message(e.getMessage())
                        .build());
    }

}
