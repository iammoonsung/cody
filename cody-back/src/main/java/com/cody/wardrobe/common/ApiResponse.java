package com.cody.wardrobe.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.http.ResponseEntity;

/**
 * API 공통 응답 객체
 * 모든 API 응답을 일관된 형식으로 제공
 *
 * 응답 구조:
 * - result: 성공(true) 또는 실패(false)
 * - data: 성공 시 반환할 데이터 (선택사항)
 * - error: 실패 시 에러 정보 (errorCode, errorMessage)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /** 성공 여부 (true: 성공, false: 실패) */
    private Boolean result;

    /** 에러 정보 (실패 시에만 포함) */
    private Error error;

    /** 응답 데이터 (성공 시 포함, 선택사항) */
    private T data;

    /**
     * 성공 응답 생성 (데이터 없음)
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 성공 응답 생성 (데이터 포함)
     * @param data 응답 데이터
     * @return 성공 응답 객체
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .result(true)
                .data(data)
                .build();
    }

    /**
     * 에러 응답 생성 (200 OK)
     * 비즈니스 로직 에러 등 예상된 에러 상황에 사용
     * @param code 에러 코드
     * @param errorMessage 에러 메시지
     * @return ResponseEntity with 200 OK
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(String code, String errorMessage) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
            .result(false)
            .error(Error.of(code, errorMessage))
            .build());
    }

    /**
     * 잘못된 요청 에러 응답 생성 (400 Bad Request)
     * 클라이언트의 잘못된 요청, 유효성 검증 실패 등에 사용
     * @param code 에러 코드
     * @param errorMessage 에러 메시지
     * @return ResponseEntity with 400 Bad Request
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String code, String errorMessage) {
        return ResponseEntity.badRequest().body(ApiResponse.<T>builder()
            .result(false)
            .error(Error.of(code, errorMessage))
            .build());
    }

    /**
     * 서버 에러 응답 생성 (500 Internal Server Error)
     * 예상치 못한 서버 내부 에러에 사용
     * @param code 에러 코드
     * @param errorMessage 에러 메시지
     * @return ResponseEntity with 500 Internal Server Error
     */
    public static <T> ResponseEntity<ApiResponse<T>> serverError(String code, String errorMessage) {
        return ResponseEntity.status(500).body(ApiResponse.<T>builder()
            .result(false)
            .error(Error.of(code, errorMessage))
            .build());
    }

    /**
     * 데이터를 포함한 에러 응답 생성
     * Validation 에러 등 추가 정보가 필요한 경우 사용
     * @param code 에러 코드
     * @param errorMessage 에러 메시지
     * @param data 추가 데이터 (예: 필드별 에러 정보)
     * @return ResponseEntity with 400 Bad Request
     */
    public static <T> ResponseEntity<ApiResponse<T>> badRequestWithData(String code, String errorMessage, T data) {
        return ResponseEntity.badRequest().body(ApiResponse.<T>builder()
            .result(false)
            .error(Error.of(code, errorMessage))
            .data(data)
            .build());
    }

    /**
     * 커스텀 HttpStatus를 가진 에러 응답 생성
     * @param status HTTP 상태 코드
     * @param code 에러 코드
     * @param errorMessage 에러 메시지
     * @return ResponseEntity with custom status
     */
    public static <T> ResponseEntity<ApiResponse<T>> error(org.springframework.http.HttpStatus status, String code, String errorMessage) {
        return ResponseEntity.status(status).body(ApiResponse.<T>builder()
            .result(false)
            .error(Error.of(code, errorMessage))
            .build());
    }

    /**
     * 에러 정보를 담는 내부 레코드
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Error(String errorCode, String errorMessage) {

        /**
         * Error 객체 생성 팩토리 메서드
         * @param errorCode 에러 코드
         * @param errorMessage 에러 메시지
         * @return Error 객체
         */
        public static Error of(String errorCode, String errorMessage) {
            return new Error(errorCode, errorMessage);
        }
    }
}
