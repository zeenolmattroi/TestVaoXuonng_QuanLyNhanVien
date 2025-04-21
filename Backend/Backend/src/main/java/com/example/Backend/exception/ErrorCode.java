package com.example.Backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

// tạo ra các mã lỗi và message tự định nghĩa
@Getter

public enum ErrorCode {
    USER_EXISTED(1001, "user đã tồn tại",HttpStatus.BAD_REQUEST),
    USER_NotEXISTED(1002, "user không tồn tại",HttpStatus.NOT_FOUND),
    LoiLungTung(9999, "Lỗi nào đó", HttpStatus.INTERNAL_SERVER_ERROR),
    InvalidKey(1006, "Lỗi nào đó trong code validate",HttpStatus.BAD_REQUEST),
    UserInvalid(1004, "username phải có từ 3-20 ký tự ",HttpStatus.BAD_REQUEST),
    UnAuthenticated(1003, "UnAuthenticated",HttpStatus.UNAUTHORIZED ),
    UnAuthorized(1005, "you dont have permission",HttpStatus.FORBIDDEN),
    PRODUCT_NotEXISTED(1007, "sản phẩm không tồn tại",HttpStatus.NOT_FOUND),
    AccountFE_EXISTED(1008,"account FE đã tồn tại",HttpStatus.BAD_REQUEST),
    AccountFPT_EXISTED(1008,"account FPT đã tồn tại",HttpStatus.BAD_REQUEST),
    CODE_EXISTED(1008,"mã nhân viên đã tồn tại",HttpStatus.BAD_REQUEST),
    ;


    ErrorCode(int code, String message,HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode=httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;




}
