package com.koa.RingDong.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private String status;     // "success" / "error"
    private String message;    // 사용자에게 전달할 메시지
    private T data;            // 실제 응답 데이터
    private Object metadata;   // (선택) 페이징 등 메타정보

    public ApiResponse(String status, String message, T data, Object metadata) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.metadata = metadata;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data, Object metadata) {
        return new ApiResponse<>("success", message, data, metadata);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null, null);
    }
}
