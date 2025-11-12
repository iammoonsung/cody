package com.cody.wardrobe.exception;

import com.cody.wardrobe.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리기
 * 모든 Controller에서 발생하는 예외를 중앙에서 처리
 *
 * 장점:
 * - ApiResponse 정적 메서드로 간결한 코드
 * - 모든 예외에 대한 상세한 로깅
 * - 타입 안전한 ResponseEntity<ApiResponse<T>>
 * - 필드별 상세한 Validation 에러 정보
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ServiceException 처리
     * 비즈니스 로직에서 발생하는 커스텀 예외
     *
     * ServiceExceptionCode에 정의된 HttpStatus와 메시지를 사용
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleServiceException(ServiceException e) {
        ServiceExceptionCode code = e.getExceptionCode();

        log.error("ServiceException: code={}, message={}, detail={}",
            code.getCode(), code.getMessage(), e.getDetailMessage());

        return ApiResponse.error(
            code.getHttpStatus(),
            code.getCode(),
            e.getMessage()
        );
    }

    /**
     * @RequestBody Validation 예외 처리
     * @Valid 어노테이션으로 검증 실패 시 발생
     *
     * 모든 필드별 에러를 Map으로 반환하여 클라이언트가 어떤 필드가 잘못되었는지 정확히 알 수 있음
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        log.warn("Validation failed for @RequestBody: {} errors", e.getErrorCount());

        // 모든 필드별 에러를 Map으로 수집
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                (existing, replacement) -> existing  // 중복 키가 있으면 첫 번째 에러 사용
            ));

        if (log.isDebugEnabled()) {
            errors.forEach((field, message) ->
                log.debug("  - {}: {}", field, message));
        }

        return ApiResponse.badRequestWithData(
            ServiceExceptionCode.INVALID_INPUT_VALUE.getCode(),
            ServiceExceptionCode.INVALID_INPUT_VALUE.getMessage(),
            errors
        );
    }

    /**
     * @ModelAttribute Validation 예외 처리
     * Form 데이터나 Query Parameter 검증 실패 시 발생
     *
     * MethodArgumentNotValidException과 동일한 방식으로 처리
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleBindException(BindException e) {

        log.warn("Validation failed for @ModelAttribute: {} errors", e.getErrorCount());

        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                (existing, replacement) -> existing
            ));

        if (log.isDebugEnabled()) {
            errors.forEach((field, message) ->
                log.debug("  - {}: {}", field, message));
        }

        return ApiResponse.badRequestWithData(
            ServiceExceptionCode.INVALID_INPUT_VALUE.getCode(),
            ServiceExceptionCode.INVALID_INPUT_VALUE.getMessage(),
            errors
        );
    }

    /**
     * 필수 파라미터 누락 예외 처리
     * @RequestParam(required=true) 파라미터가 없을 때 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParameterException(
            MissingServletRequestParameterException e) {

        log.warn("Missing required parameter: name={}, type={}",
            e.getParameterName(), e.getParameterType());

        return ApiResponse.badRequest(
            ServiceExceptionCode.MISSING_REQUIRED_PARAMETER.getCode(),
            ServiceExceptionCode.MISSING_REQUIRED_PARAMETER.getMessage() + ": " + e.getParameterName()
        );
    }

    /**
     * IllegalArgumentException 처리
     * 잘못된 인자 전달 시 발생하는 일반적인 예외
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {

        log.warn("Illegal argument: {}", e.getMessage());

        return ApiResponse.badRequest(
            ServiceExceptionCode.INVALID_INPUT_VALUE.getCode(),
            e.getMessage() != null ? e.getMessage() : "잘못된 인자입니다."
        );
    }

    /**
     * IllegalStateException 처리
     * 잘못된 상태에서 메서드 호출 시 발생
     *
     * 주로 코디 상태 검증 등의 비즈니스 상태 검증에서 발생
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException e) {

        log.warn("Illegal state: {}", e.getMessage());

        return ApiResponse.badRequest(
            ServiceExceptionCode.INVALID_INPUT_VALUE.getCode(),
            e.getMessage() != null ? e.getMessage() : "잘못된 상태입니다."
        );
    }

    /**
     * 예상하지 못한 모든 예외 처리
     *
     * 위에서 처리되지 않은 모든 예외를 캐치
     * 실제 에러 메시지는 로그에만 기록하고 클라이언트에는 일반적인 메시지만 반환 (보안)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {

        log.error("Unexpected error occurred: {}", e.getMessage(), e);

        return ApiResponse.serverError(
            ServiceExceptionCode.INTERNAL_SERVER_ERROR.getCode(),
            ServiceExceptionCode.INTERNAL_SERVER_ERROR.getMessage()
        );
    }
}
