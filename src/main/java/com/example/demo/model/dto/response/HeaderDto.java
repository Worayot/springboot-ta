package com.example.demo.model.dto.response;

public class HeaderDto {
    private int code;
    private String message;

    public HeaderDto() {}
        
    public HeaderDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) { this.code = code; }
    public int getCode() { return this.code; }
    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return this.message; }
}
