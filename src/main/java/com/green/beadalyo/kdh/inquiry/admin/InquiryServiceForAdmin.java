package com.green.beadalyo.kdh.inquiry.admin;

import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryListForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryOneForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.InquiryListDto;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.admin.model.PostInquiryResponseForAdminReq;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.green.beadalyo.gyb.common.ConstVariable.PAGE_SIZE;

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

    public Page<InquiryEntity> getInquiryListForAdmins(Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        return inquiryRepository.findAllByOrderByInquiryPkDesc(pageable);
    }

    public List<GetInquiryListForAdmin> makeGetInquiryListForAdmin(Page<InquiryEntity> pageEntity){
        List<GetInquiryListForAdmin> result = new ArrayList();
        for(InquiryEntity inquiry : pageEntity){
            GetInquiryListForAdmin getInquiryListForAdmin = new GetInquiryListForAdmin(
                    inquiry.getInquiryPk(),
                    inquiry.getInquiryTitle(),
                    inquiry.getInquiryState(),
                    inquiry.getCreatedAt(),
                    inquiry.getUpdatedAt()
            );
            result.add(getInquiryListForAdmin);
        }
        return result;
    }

    public InquiryListDto makeInquiryListDto(InquiryListDto inquiryListDto, Page<InquiryEntity> pageEntity,
                                             List<GetInquiryListForAdmin> resultList ){
        inquiryListDto.setTotalPage(pageEntity.getTotalPages());
        inquiryListDto.setTotalElements(pageEntity.getNumberOfElements());
        inquiryListDto.setResult(resultList);

        return inquiryListDto;
    }

    public Page<InquiryEntity> getInquiryListFinishedForAdmins(Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        return inquiryRepository.findByInquiryStateOrderByInquiryPkDesc(2, pageable);
    }

    public Page<InquiryEntity> getInquiryListUnfinishedForAdmins(Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        return inquiryRepository.findByInquiryStateOrderByInquiryPkDesc(1, pageable);
    }

}
