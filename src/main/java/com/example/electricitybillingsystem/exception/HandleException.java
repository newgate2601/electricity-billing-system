package com.example.electricitybillingsystem.exception;

import com.example.electricitybillingsystem.vo.ExceptionVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ExceptionVO> handleIllegalArgumentException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ExceptionVO.builder()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(),
                HttpStatus.FORBIDDEN);
    }

}
