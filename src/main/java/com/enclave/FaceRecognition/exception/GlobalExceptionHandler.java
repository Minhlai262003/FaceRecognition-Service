package com.enclave.FaceRecognition.exception;

import com.enclave.FaceRecognition.dto.Response.ApiResponse;
import feign.FeignException;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.View;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }
        @ExceptionHandler(value = AppException.class)
        ResponseEntity<ApiResponse> handlingAppException(AppException exception){

        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setSuccess(false);
        return ResponseEntity.badRequest().body(apiResponse);
    }
//
//
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
//        String errorKey = exception.getFieldError().getDefaultMessage();
//        ErrorCode errorCode = ErrorCode.valueOf(errorKey);
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//    @ExceptionHandler(value = FeignException.class)
//    ResponseEntity<ApiResponse> handlingFeignException(FeignException exception) {
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(1001);
//        apiResponse.setMessage(exception.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(404, ex.getMessage(), false, null));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//                ApiResponse.<String>builder()
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                        .message("Internal server error")
//                        .success(false)
//                        .data(null)
//                        .build()
//        );
//    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        String errorMessage = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getDefaultMessage())
//                .collect(Collectors.joining(", "));
//        return ResponseEntity.badRequest().body(ApiResponse.<Void>builder().status(HttpStatus.BAD_REQUEST.value()).message(errorMessage).success(false).data(null).build());
//    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiResponse.<String>builder()
                        .status(HttpStatus.UNAUTHORIZED.value()) // 401
                        .message("Unauthorized")
                        .success(false)
                        .data(null)
                        .build()
        );
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(401, ex.getMessage(), false, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(new ApiResponse(400, errorMessage, false, null));
    }

    @ExceptionHandler(ConfigDataResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ConfigDataResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, ex.getMessage(), false, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(500, "Internal server error", false, null));
    }
    @ExceptionHandler(PythonServiceValidationException.class)
    public ResponseEntity<ApiResponse<?>> handlePythonServiceValidation(PythonServiceValidationException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(400, ex.getMessage(), false, null));
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateField(DuplicateFieldException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), ex.getMessage(), false, null));
    }

}
