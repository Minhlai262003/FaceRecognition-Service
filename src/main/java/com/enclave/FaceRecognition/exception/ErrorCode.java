package com.enclave.FaceRecognition.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception"),
    EMAIL_EXISTED(1001, "Email existed"),
    EMAIL_NULL(1002, "Email cannot be null"),
    FIRST_NAME_NULL(1003, "First name cannot be null"),
    LAST_NAME_NULL(1004, "Last name cannot be null"),
    PASSWORD_NULL(1005, "Password cannot be null"),
    PYTHON_SERVICE_ERROR(1006, "Python service error"),
    PHONE_INVALID(1007, "Phone number is invalid"),
    FACE_RECOGNITION_ERROR(1008, "Face recognition error"),
    USER_DO_NOT_EXIST(1009, "User do not exist")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
