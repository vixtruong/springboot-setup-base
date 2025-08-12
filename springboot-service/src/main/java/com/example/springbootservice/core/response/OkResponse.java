package com.example.springbootservice.core.response;

import com.example.springbootservice.core.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OkResponse {
    final boolean success = true;

    @Builder.Default
    int code = SuccessCode.OK.getCode();

    String message;
    Object data;

    public OkResponse(Object data) {
        this.data = data;
    }
}

