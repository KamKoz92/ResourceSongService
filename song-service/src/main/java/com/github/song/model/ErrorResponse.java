package com.github.song.model;

import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponse {
    String errorMessage;
    Map<String, String> details;
    String errorCode;
}
