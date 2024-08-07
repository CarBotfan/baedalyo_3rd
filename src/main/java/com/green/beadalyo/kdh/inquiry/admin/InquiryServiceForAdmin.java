package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseForAdminReq;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryServiceForAdmin {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;


    public Integer saveInquiry(InquiryEntity entity){
        inquiryRepository.save(entity);
        return 1;
    }

    public InquiryEntity makeInquiryResponse(PostInquiryResponseForAdminReq p){
        InquiryEntity entity = inquiryRepository.getReferenceById(p.getInquiryPk());
        entity.setInquiryResponse(p.getInquiryResponse());
        entity.setInquiryState(2);
        return entity;
    }


}
