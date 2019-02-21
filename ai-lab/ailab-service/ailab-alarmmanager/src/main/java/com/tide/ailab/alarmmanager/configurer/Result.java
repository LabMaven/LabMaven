package com.tide.ailab.alarmmanager.configurer;

public class Result {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Result(String code, String message) {
        this.message = message;
        this.code = code;
    }
}
