package com.example.springbootservice.core;

import java.util.HashMap;
import java.util.Map;

public class AppConstants {
    public static class SuccessCode {
        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int ACCEPTED = 202;
        public static final int NO_CONTENT = 204;
    }

    public static class ExceptionType {
        public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
        public static final String ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND";
        public static final String UNAUTHORIZED = "UNAUTHORIZED";
        public static final String FORBIDDEN = "FORBIDDEN";
        public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
        public static final String BAD_REQUEST = "BAD_REQUEST";
        public static final String DUPLICATE_RESOURCE = "DUPLICATE_RESOURCE";
        public static final String CONFLICT = "CONFLICT";
        public static final String TIMEOUT = "TIMEOUT";
        public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";

        public static final Map<String, Integer> CODE_MAP = new HashMap<>() {{
            put(VALIDATION_ERROR, 400);
            put(ENTITY_NOT_FOUND, 404);
            put(UNAUTHORIZED, 401);
            put(FORBIDDEN, 403);
            put(INTERNAL_ERROR, 500);
            put(BAD_REQUEST, 400);
            put(DUPLICATE_RESOURCE, 409);
            put(CONFLICT, 409);
            put(TIMEOUT, 408);
            put(SERVICE_UNAVAILABLE, 503);
        }};
    }

    public static class Roles {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }
}
