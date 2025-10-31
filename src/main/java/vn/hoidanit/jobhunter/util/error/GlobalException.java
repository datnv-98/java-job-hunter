package vn.hoidanit.jobhunter.util.error;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.hoidanit.jobhunter.domain.ApiResponse;
import vn.hoidanit.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exception occurs ...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // <?> tự đoán kiểu dữ liệu
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NoSuchElementException ex) {
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Not Found", null,
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        RestResponse<Object> response = new RestResponse<Object>();
        response.setError(ex.getBody().getDetail());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(errorList.size() > 1 ? errorList : errorList.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Global exception handler, khi không bắt được các exception cụ thể
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAllException(Exception ex) {
        ApiResponse<?> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null,
                "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
