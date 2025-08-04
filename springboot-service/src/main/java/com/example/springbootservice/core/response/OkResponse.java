package com.example.springbootservice.core.response;

import com.example.springbootservice.core.AppConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkResponse<T> {
    final boolean success = true;

    @Builder.Default
    int code = AppConstants.SuccessCode.OK;

    String message;
    T data;
}

