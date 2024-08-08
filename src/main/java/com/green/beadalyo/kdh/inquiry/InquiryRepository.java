package com.green.beadalyo.kdh.inquiry;

import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryOneForUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {

    @Query(value = "SELECT   i.inquiry_pk AS inquiryPK, "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "where i.user_pk = :userPk "+
                            "ORDER BY i.inquiry_pk DESC",nativeQuery = true)
     List<GetInquiryListForUser> findInquiryListByUserPk(Long userPk);


    @Query(value = "SELECT   i.inquiry_pk AS inquiryPK, "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_content AS inquiryContent, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.inquiry_response AS inquiryResponse, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "where i.inquiry_pk = :inquiryPk ",nativeQuery = true)
    GetInquiryOneForUser findInquiryOneByUserPk(Long inquiryPk);
}
