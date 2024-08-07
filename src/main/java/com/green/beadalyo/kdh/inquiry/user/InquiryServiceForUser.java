package com.green.beadalyo.kdh.inquiry.user;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryOneForUser;
import com.green.beadalyo.kdh.inquiry.user.model.PostInquiryForUserReq;
import com.green.beadalyo.kdh.inquiry.user.model.PutInquiryForUserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceForUser {
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    public User getUserByPk(Long userPk){
        return userRepository.getReferenceById(userPk);
    }

    public InquiryEntity makeInquiryForPost(PostInquiryForUserReq p){
        InquiryEntity entity = new InquiryEntity();
        entity.setInquiryTitle(p.getInquiryTitle());
        entity.setInquiryContent(p.getInquiryContent());
        entity.setUserPk(p.getUser());
        entity.setInquiryState(1);

        return entity;
    }

    public Integer saveInquiry(InquiryEntity entity){
        inquiryRepository.save(entity);
        return 1;
    }

    public InquiryEntity makeInquiryForPut(PutInquiryForUserReq p){
        InquiryEntity entity = inquiryRepository.getReferenceById(p.getInquiryPk());
        if (entity.getUserPk() != p.getUser()){
            throw new RuntimeException();
        }
        entity.setInquiryTitle(p.getInquiryTitle());
        entity.setInquiryContent(p.getInquiryContent());
        entity.setUserPk(p.getUser());

        return entity;
    }

    public List<GetInquiryListForUser> getInquiryListForUser(Long userPk){
        return inquiryRepository.findInquiryListByUserPk(userPk);
    }

    public GetInquiryOneForUser getInquiryOneForUser(Long inquiryPk){
        return inquiryRepository.findInquiryOneByUserPk(inquiryPk);
    }
    public boolean checkUser(Long inquiryPk){
        Long userPk = authenticationFacade.getLoginUserPk();
        InquiryEntity inquiryEntity = inquiryRepository.getReferenceById(inquiryPk);
        if (inquiryEntity.getUserPk().getUserPk() != userPk){
            return false;
        }
        return true;
    }

    public void delInquiry(Long inquiryPk){
        InquiryEntity inquiryEntity = inquiryRepository.getReferenceById(inquiryPk);
        if (inquiryEntity.getInquiryPk() == 2){
            throw new RuntimeException();
        }
        inquiryRepository.delete(inquiryEntity);
    }
}
