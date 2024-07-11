package com.example.cardValidation.exception;


import com.example.cardValidation.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String errorMessage = "Unknown error";

        try (InputStream bodyIs = response.body().asInputStream()) {
            ErrorDto errorDto = objectMapper.readValue(bodyIs, ErrorDto.class);
            errorMessage = errorDto.getMessage();
        } catch (IOException e) {
            return new CustomException("Failed to parse error response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        switch (status) {
            case NOT_FOUND:
                return ApiError.notFound(errorMessage);
            case BAD_REQUEST:
                return ApiError.badRequest(errorMessage);
            case UNAUTHORIZED:
                return ApiError.unauthorized(errorMessage);
            case FORBIDDEN:
                return ApiError.forbidden(errorMessage);
            case INTERNAL_SERVER_ERROR:
                return ApiError.internalServerError(errorMessage);
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}
