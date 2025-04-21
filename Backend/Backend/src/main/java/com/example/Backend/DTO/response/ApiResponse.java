package com.example.Backend.DTO.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    int code = 1000; //mawcjcj định nếu ko truyền
    String message;
    T result;

//    int code = HttpStatus.OK.value(); // Mặc định là mã thành công (200)
//    String message = "Thành công"; // Mặc định là thông báo thành công
//    T result;
//
//
//    public static <T> ApiResponseBuilder<T> builderWithCode(int code) {
//        ApiResponseBuilder<T> builder = builder();
//        builder.code(code);
//        return builder;
//    }
//
//    public static <T> ApiResponseBuilder<T> builderWithMessage(String message) {
//        ApiResponseBuilder<T> builder = builder();
//        builder.message(message);
//        return builder;
//    }
//
//    public static <T> ApiResponseBuilder<T> builderWithCodeAndMessage(int code, String message) {
//        ApiResponseBuilder<T> builder = builder();
//        builder.code(code);
//        builder.message(message);
//        return builder;
//    }


}