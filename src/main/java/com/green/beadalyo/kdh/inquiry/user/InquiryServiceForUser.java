package com.green.beadalyo.kdh.inquiry.user;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.user.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.green.beadalyo.gyb.common.ConstVariable.PAGE_SIZE;

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
        entity.setUser(p.getUser());
        entity.setInquiryState(1);

        return entity;
    }

    public Integer saveInquiry(InquiryEntity entity){
        inquiryRepository.save(entity);
        return 1;
    }

    public InquiryEntity makeInquiryForPut(PutInquiryForUserReq p){
        InquiryEntity entity = inquiryRepository.getReferenceById(p.getInquiryPk());
        if (entity.getUser() != p.getUser()){
            throw new RuntimeException();
        }
        entity.setInquiryTitle(p.getInquiryTitle());
        entity.setInquiryContent(p.getInquiryContent());
        entity.setUser(p.getUser());

        return entity;
    }

    public Page<InquiryEntity> getInquiryListForUsers(Long userPk, Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        User user = getUserByPk(userPk);
        return inquiryRepository.findByUserOrderByInquiryPkDesc(user, pageable);
    }

    public List<GetInquiryListForUser> makeGetInquiryListForUser(Page<InquiryEntity> pageEntity) {
        List<GetInquiryListForUser> result = new ArrayList<>();
        for (InquiryEntity inquiry : pageEntity) {
            GetInquiryListForUser getInquiryListForUser = new GetInquiryListForUser(
                    inquiry.getInquiryPk(),
                    inquiry.getCreatedAt(),
                    inquiry.getInquiryState(),
                    inquiry.getInquiryTitle(),
                    inquiry.getUpdatedAt()
            );
            result.add(getInquiryListForUser);
        }
        return result;
    }

    public InquiryListDto makeInquiryListDto(InquiryListDto inquiryListDto, Page<InquiryEntity> pageEntity,
                                             List<GetInquiryListForUser> resultList ){
        inquiryListDto.setTotalPage(pageEntity.getTotalPages());
        inquiryListDto.setTotalElements(pageEntity.getNumberOfElements());
        inquiryListDto.setResult(resultList);

        return inquiryListDto;
    }

    public GetInquiryOneForUser getInquiryOneForUser(Long inquiryPk){
        return inquiryRepository.findInquiryOneByUserPk(inquiryPk);
    }
    public boolean checkUser(Long inquiryPk){
        Long userPk = authenticationFacade.getLoginUserPk();
        InquiryEntity inquiryEntity = inquiryRepository.getReferenceById(inquiryPk);
        if (!inquiryEntity.getUser().getUserPk().equals(userPk) ){
            return false;
        }
//        Optional<InquiryEntity> inquiryEntity = inquiryRepository.findById(inquiryPk);
//        Long entityUserPk = inquiryEntity.get().getUser().getUserPk();
//        if (!entityUserPk.equals(userPk)) {
//            return false;
//        }
        return true;
    }

    public void delInquiry(Long inquiryPk){
        InquiryEntity inquiryEntity = inquiryRepository.getReferenceById(inquiryPk);
        if (inquiryEntity.getInquiryState() == 2){
            throw new RuntimeException();
        }
        inquiryRepository.delete(inquiryEntity);
    }
}
