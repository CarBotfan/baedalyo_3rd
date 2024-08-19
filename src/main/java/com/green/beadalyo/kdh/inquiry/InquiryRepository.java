package com.green.beadalyo.kdh.inquiry;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryListForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryOneForAdmin;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser;
import com.green.beadalyo.kdh.inquiry.user.model.GetInquiryOneForUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {

//    @Query( "SELECT new com.green.beadalyo.kdh.inquiry.user.model.GetInquiryListForUser( " +
//            "i.inquiryPk, i.createdAt, i.inquiryState, i.inquiryTitle, i.updatedAt) "+
//            "FROM InquiryEntity i "+
//            "where i.user.userPk = :userPk "+
//            "ORDER BY i.inquiryPk DESC")
//    Page<GetInquiryListForUser> findInquiryListByUserPk(Long userPk, Pageable pageable);

    Page<InquiryEntity> findByUserOrderByInquiryPkDesc(User user, Pageable pageable) ;

    Page<InquiryEntity> findByInquiryStateOrderByInquiryPkDesc(Integer state, Pageable pageable) ;

    Page<InquiryEntity> findAllByOrderByInquiryPkDesc(Pageable pageable );

    @Query(value = "SELECT  "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_content AS inquiryContent, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.inquiry_response AS inquiryResponse, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "where i.inquiry_pk = :inquiryPk ",nativeQuery = true)
    GetInquiryOneForUser findInquiryOneByUserPk(Long inquiryPk);



    @Query(value = "SELECT  "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_content AS inquiryContent, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.inquiry_response AS inquiryResponse, "+
                            "(SELECT user_nickname FROM user WHERE user_pk = i.user_pk) AS inquiryNickName, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "where i.inquiry_pk = :inquiryPk ",nativeQuery = true)
    GetInquiryOneForAdmin findInquiryOneForAdmin(Long inquiryPk);
}
