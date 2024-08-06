package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryReq;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseReq;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    public User getUserByPk(Long userPk){
        return userRepository.getReferenceById(userPk);
    }

    public InquiryEntity makeInquiry(PostInquiryReq p){
        InquiryEntity entity = new InquiryEntity();
        entity.setInquiryTitle(p.getInquiryTitle());
        entity.setInquiryContent(p.getInquiryContent());
        entity.setUserPk(p.getUser());

        return entity;
    }

    public Integer postInquiry(InquiryEntity entity){
        inquiryRepository.save(entity);
        return 1;
    }

    public InquiryEntity makeInquiryResponse(PostInquiryResponseReq p){
        InquiryEntity entity = new InquiryEntity();
        entity.setInquiryPk(p.getInquiryPk());
        entity.setInquiryResponse(p.getInquiryResponse());
        entity.setInquiryState(2);
        return entity;
    }

}
