package com.green.beadalyo.kdh.inquiry.admin.model;

import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser;
import lombok.Data;

import java.util.List;

@Data
public class InquiryListDto {
    private int totalElements;
    private int totalPage;
    private List<GetInquiryListForAdmin> result;
}
