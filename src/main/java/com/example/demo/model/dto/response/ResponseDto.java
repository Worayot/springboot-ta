package com.example.demo.model.dto.response;

public class ResponseDto<T> {
    private HeaderDto header;
    private T data;

    // Constructor
    public ResponseDto() {}

    public ResponseDto(HeaderDto header, T data) {
        this.header = header;
        this.data = data;
    }

    // Getter for header
    public HeaderDto getHeader() {
        return header;
    }

    // Setter for header
    public void setHeader(HeaderDto header) {
        this.header = header;
    }

    // Getter for data
    public T getData() {
        return data;
    }

    // Setter for data
    public void setData(T data) {
        this.data = data;
    }
}
