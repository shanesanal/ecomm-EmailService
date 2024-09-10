package com.ecommerce.emailservice.dto;

import lombok.Data;

@Data
public class SendEmailDto {
    private String subject;
    private String from ;
    private String to;
    private String body;

}
