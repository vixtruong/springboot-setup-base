package com.example.springbootservice.core;

public class AppConstants {
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
    }

    public static class Roles {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }
}
