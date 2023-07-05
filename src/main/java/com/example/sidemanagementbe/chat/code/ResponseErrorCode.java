package com.example.sidemanagementbe.chat.code;

import com.example.sidemanagementbe.chat.dto.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseErrorCode {
    private Status status;
}
