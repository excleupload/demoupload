package com.example.tapp.common.response;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import com.example.tapp.ws.common.response.ResponseStatus;

public class ResponseUtils {

    /**
     * Success Response
     */
    public static Function<Object, ResponseEntity<?>> success = (object) -> {
        Response<Object> response = new Response<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(object);
        return ResponseEntity.ok(response);
    };

    /**
     * Success Response with status
     */
    public static BiFunction<Object, HttpStatus, ResponseEntity<?>> succesWithStatus = (object, httpStatus) -> {
        Response<Object> response = new Response<>();
        response.setStatusCode(httpStatus.value());
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(object);
        return ResponseEntity.ok(response);
    };

    /**
     * Error Response
     */
    public static Function<Object, ResponseEntity<?>> error = (object) -> {
        Response<Object> response = new Response<>();
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setStatus(ResponseStatus.ERROR);
        response.setData(object);
        return ResponseEntity.ok(response);
    };

    /**
     * Error Response with status
     */
    public static BiFunction<Object, HttpStatus, Response<Object>> errorMessageWithStatus = (object, httpStatus) -> {
        Response<Object> response = new Response<>();
        response.setStatusCode(httpStatus.value());
        response.setStatus(ResponseStatus.ERROR);
        response.setData(object);
        return response;
    };

    /**
     * Error Response with status
     */
    public static BiFunction<Object, HttpStatus, ResponseEntity<?>> errorWithStatus = (object, httpStatus) -> {
        Response<Object> response = new Response<>();
        response.setStatusCode(httpStatus.value());
        response.setStatus(ResponseStatus.ERROR);
        response.setData(object);
        return ResponseEntity.ok(response);
    };

    public static Function<List<FieldError>, ResponseEntity<?>> errorList = (fieldErrors) -> {
        Response<HashMap<String, String>> response = new Response<>();
        HashMap<String, String> errors = new HashMap<>();
        fieldErrors.stream().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        response.setStatus(ResponseStatus.ERROR);
        response.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setData(errors);
        return ResponseEntity.ok(response);
    };

    public static void sendImage(Object[] objects, HttpServletResponse response) throws IOException {
        byte[] bs = (byte[]) objects[0];
        InputStream inputStream = new ByteArrayInputStream((bs));
        response.setContentLengthLong(bs.length);
        OutputStream outStream = response.getOutputStream();
        byte[] buffer = new byte[bs.length];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outStream.close();
    }
}