package com.cody.wardrobe.exception;

import lombok.Getter;

/**
 * 비즈니스 로직에서 발생하는 커스텀 예외
 */
@Getter
public class ServiceException extends RuntimeException {

    private final ServiceExceptionCode exceptionCode;
    private final String detailMessage;

    /**
     * 기본 생성자
     * @param exceptionCode 예외 코드
     */
    public ServiceException(ServiceExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.detailMessage = null;
    }

    /**
     * 상세 메시지를 포함하는 생성자
     * @param exceptionCode 예외 코드
     * @param detailMessage 상세 메시지 (ID, 추가 정보 등)
     */
    public ServiceException(ServiceExceptionCode exceptionCode, String detailMessage) {
        super(exceptionCode.getMessage() + " " + detailMessage);
        this.exceptionCode = exceptionCode;
        this.detailMessage = detailMessage;
    }

    /**
     * 원인 예외를 포함하는 생성자
     * @param exceptionCode 예외 코드
     * @param cause 원인 예외
     */
    public ServiceException(ServiceExceptionCode exceptionCode, Throwable cause) {
        super(exceptionCode.getMessage(), cause);
        this.exceptionCode = exceptionCode;
        this.detailMessage = null;
    }
}
