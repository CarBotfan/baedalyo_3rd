package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryListForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryOneForAdmin;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseForAdminReq;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public GetInquiryOneForAdmin getInquiryOneForAdmin(Long inquiryPk){
        return inquiryRepository.findInquiryOneForAdmin(inquiryPk);
    }

    public List<GetInquiryListForAdmin> getInquiryListForAdmins(){
        return inquiryRepository.findInquiryListForAdmin();
    }

    public List<GetInquiryListForAdmin> getInquiryListFinishedForAdmins(){
        return inquiryRepository.findInquiryListFinishedForAdmin();
    }

    public List<GetInquiryListForAdmin> getInquiryListUnfinishedForAdmins(){
        return inquiryRepository.findInquiryListUnfinishedForAdmin();
    }

}
