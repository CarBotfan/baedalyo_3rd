package com.green.beadalyo.kdh.admin.inquiry;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.admin.entity.InquiryEntity;
import com.green.beadalyo.kdh.admin.inquiry.model.PostInquiryReq;
import com.green.beadalyo.kdh.admin.repository.InquiryRepository;
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

    public Long postInquiry(InquiryEntity entity){
       return inquiryRepository.save(entity).getInquiryPk();
    }
}
