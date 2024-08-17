package com.green.beadalyo.kdh.inquiry;

import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryListForAdmin;
import com.green.beadalyo.kdh.inquiry.admin.model.GetInquiryOneForAdmin;
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
//                            "AND i.inquiry_state = 1"+
                            "ORDER BY i.inquiry_pk DESC",nativeQuery = true)
     List<GetInquiryListForUser> findInquiryListByUserPk(Long userPk);


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

    @Query(value = "SELECT   i.inquiry_pk AS inquiryPK, "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "ORDER BY i.inquiry_pk DESC",nativeQuery = true)
    List<GetInquiryListForAdmin> findInquiryListForAdmin();

    @Query(value = "SELECT   i.inquiry_pk AS inquiryPK, "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "where i.inquiry_state = 1 "+
                            "ORDER BY i.inquiry_pk DESC",nativeQuery = true)
    List<GetInquiryListForAdmin> findInquiryListUnfinishedForAdmin();

    @Query(value = "SELECT   i.inquiry_pk AS inquiryPK, "+
                            "i.inquiry_title AS inquiryTitle, "+
                            "i.inquiry_state AS inquiryState, "+
                            "i.updated_at AS updatedAt, "+
                            "i.created_at AS createdAt "+
                            "FROM inquiry i "+
                            "where i.inquiry_state = 2 "+
                            "ORDER BY i.inquiry_pk DESC",nativeQuery = true)
    List<GetInquiryListForAdmin> findInquiryListFinishedForAdmin();

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
