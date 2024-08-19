package com.green.beadalyo.kdh.inquiry.admin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetInquiryListForAdmin {
   private Long inquiryPk;
   private String inquiryTitle;
   private Integer inquiryState;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

    public GetInquiryListForAdmin(Long inquiryPk, String inquiryTitle, Integer inquiryState, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.inquiryPk = inquiryPk;
        this.inquiryTitle = inquiryTitle;
        this.inquiryState = inquiryState;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


}
