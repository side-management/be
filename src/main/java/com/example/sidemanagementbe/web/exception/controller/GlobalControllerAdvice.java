package com.example.sidemanagementbe.web.exception.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpServletRequest request) {
        log.warn("invalid payload request url:{}, trace:{}", request.getRequestURI(), methodArgumentNotValidException.getStackTrace());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(methodArgumentNotValidException.getBindingResult()));
    }


    @RequiredArgsConstructor
    @Getter
    static class ErrorResponse {

        private final List<String> validations;

        public static ErrorResponse from(BindingResult bindingResult) {
            return new ErrorResponse(bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()));
        }
    }
}
