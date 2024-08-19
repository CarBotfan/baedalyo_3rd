package com.green.beadalyo.kdh.inquiry.user.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetInquiryListForUser {
    private Long inquiryPk;
    private LocalDateTime createdAt;
    private Integer inquiryState;
    private String inquiryTitle;
    private LocalDateTime updatedAt;

    public GetInquiryListForUser(Long inquiryPk, LocalDateTime createdAt, Integer inquiryState, String inquiryTitle, LocalDateTime updatedAt) {
        this.inquiryPk = inquiryPk;
        this.createdAt = createdAt;
        this.inquiryState = inquiryState;
        this.inquiryTitle = inquiryTitle;
        this.updatedAt = updatedAt;
    }
}
