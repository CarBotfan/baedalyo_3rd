package com.green.beadalyo.kdh.inquiry.user.model;

import lombok.Data;

import java.util.List;

@Data
public class InquiryListDto {
    private int totalElements;
    private int totalPage;
    private List<GetInquiryListForUser> result;
}
