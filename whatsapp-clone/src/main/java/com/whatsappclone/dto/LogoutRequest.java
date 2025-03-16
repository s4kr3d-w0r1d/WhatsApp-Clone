package com.whatsappclone.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LogoutRequest {
    private String email;
}
