package com.cody.wardrobe.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 예외 코드 및 메시지 정의
 * 모든 예외 상황을 한 곳에서 관리
 */
@Getter
@RequiredArgsConstructor
public enum ServiceExceptionCode {

    // Item 관련 예외
    ITEM_NOT_FOUND("ITEM_001", "아이템을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_ITEM("ITEM_002", "이미 존재하는 아이템입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ITEM_CATEGORY("ITEM_003", "잘못된 카테고리입니다.", HttpStatus.BAD_REQUEST),

    // Outfit 관련 예외
    OUTFIT_NOT_FOUND("OUTFIT_001", "코디를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    OUTFIT_ITEMS_REQUIRED("OUTFIT_002", "코디에는 최소 1개 이상의 아이템이 필요합니다.", HttpStatus.BAD_REQUEST),
    INVALID_RATING("OUTFIT_003", "평점은 1~5 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_FORMALITY_LEVEL("OUTFIT_004", "격식도는 1~5 사이여야 합니다.", HttpStatus.BAD_REQUEST),

    // History 관련 예외
    HISTORY_NOT_FOUND("HISTORY_001", "착용 기록을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_HISTORY("HISTORY_002", "해당 날짜에 이미 착용 기록이 존재합니다.", HttpStatus.BAD_REQUEST),

    // Recommendation 관련 예외
    NO_RECOMMENDATION_FOUND("RECOMMEND_001", "추천 가능한 코디가 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_RECOMMENDATION_CRITERIA("RECOMMEND_002", "추천 조건이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // Validation 관련 예외
    INVALID_INPUT_VALUE("COMMON_001", "입력값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_PARAMETER("COMMON_002", "필수 파라미터가 누락되었습니다.", HttpStatus.BAD_REQUEST),

    // 일반 예외
    INTERNAL_SERVER_ERROR("COMMON_999", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
