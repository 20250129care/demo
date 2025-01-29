package com.example.demo.controller.advice;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.common.exception.ApplicationException;
import com.example.demo.common.response.ErrorResponse;

import lombok.RequiredArgsConstructor;

/**
 * エラーレスポンスの作成処理。
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonRestControllerAdvice extends ResponseEntityExceptionHandler {

    /** 区切り文字。 */
    private static final String SEP = ":";

    /** メッセージソース。 */
    private final MessageSource messageSource;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorId = "901";
        String errorMessage = messageSource.getMessage(errorId, null, Locale.JAPAN);

        logger.error(errorId + SEP + errorMessage, ex);

        List<String> data = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + SEP + fieldError.getDefaultMessage())
                .toList();

        ErrorResponse response = new ErrorResponse(errorId, errorMessage, data);

        return super.handleExceptionInternal(ex, response, headers, status, request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String errorId = "903";
        String errorMessage = messageSource.getMessage(errorId, null, Locale.JAPAN);

        logger.error(errorId + SEP + errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorId, errorMessage);

        return super.handleExceptionInternal(ex, response, headers, status, request);
    }

    /**
     * リクエストのヘッダの必須項目が設定されていない場合に呼び出される。
     * 
     * @param ex MissingRequestHeaderException
     * @param request WebRequest
     * @return ResponseEntity
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex,
            WebRequest request) {
        String errorId = "904";
        String errorMessage = messageSource.getMessage(errorId, null, Locale.JAPAN);

        logger.error(errorId + SEP + errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorId, errorMessage);

        HttpHeaders httpHeaders = new HttpHeaders();

        return super.handleExceptionInternal(ex, response, httpHeaders, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * 業務エラーが発生した場合に呼び出される。
     * 
     * @param ex ApplicationException
     * @param request WebRequest
     * @return ResponseEntity
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
        String errorId = ex.getErrorId();
        String errorMessage = ex.getErrorMessage();

        logger.error(errorId + SEP + errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorId, errorMessage);

        HttpHeaders httpHeaders = new HttpHeaders();

        return super.handleExceptionInternal(ex, response, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * DBエラーが発生した場合に呼び出される。
     * 
     * @param ex DataAccessException
     * @param request WebRequest
     * @return ResponseEntity
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        String errorId = "902";
        String errorMessage = messageSource.getMessage(errorId, null, Locale.JAPAN);

        logger.error(errorId + SEP + errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorId, errorMessage);

        HttpHeaders httpHeaders = new HttpHeaders();

        return super.handleExceptionInternal(ex, response, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 想定外のエラーが発生した場合に呼び出される。
     * 
     * @param ex Exception
     * @param request WebRequest
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleSystemError(Exception ex, WebRequest request) {
        String errorId = "999";
        String errorMessage = messageSource.getMessage(errorId, null, Locale.JAPAN);

        logger.error(errorId + SEP + errorMessage, ex);

        ErrorResponse response = new ErrorResponse(errorId, errorMessage);

        HttpHeaders httpHeaders = new HttpHeaders();

        return super.handleExceptionInternal(ex, response, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
