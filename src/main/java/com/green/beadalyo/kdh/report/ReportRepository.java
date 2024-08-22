package com.green.beadalyo.kdh.report;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import com.green.beadalyo.kdh.report.admin.model.GetReportListResForAdmin;
import com.green.beadalyo.kdh.report.admin.model.GetReportOneResForAdmin;
import com.green.beadalyo.kdh.report.user.model.GetReportListResForUser;
import com.green.beadalyo.kdh.report.user.model.GetReportOneResForUser;
import com.green.beadalyo.lhn.Review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    Integer countByReviewPk(Review reviewPk);

    Integer countByReviewPkAndUser(Review reviewPk, User user);

    Page<ReportEntity> findAllByOrderByReportPkDesc(Pageable pageable);

    Page<ReportEntity> findByUserOrderByReportPkDesc(User user, Pageable pageable);

    Page<ReportEntity> findByReportStateOrderByReportPkDesc(Integer state, Pageable pageable);

    @Query(value = "SELECT   r.report_title AS reportTitle, "+
                            "r.report_content AS reportContent, "+
                            "(SELECT user_nickname FROM user WHERE user_pk = r.user_pk) AS reportNickName, "+
                            "r.updated_at AS reportUpdatedAt, "+
                            "r.created_at AS reportCreatedAt, "+
                            "v.review_contents AS reviewContents, "+
                            "v.review_pics_1 AS reviewPics1, "+
                            "v.review_pics_2 AS reviewPics2, "+
                            "v.review_pics_3 AS reviewPics3, "+
                            "v.review_pics_4 AS reviewPics4, "+
                            "(SELECT user_nickname FROM user WHERE user_pk = v.user_pk) AS reviewNickName, "+
                            "(select user_pk FROM user where user_pk = v.user_pk) AS reviewUserPk, "+
                            "r.report_state AS reportState, "+
                            "v.created_at AS reviewCreatedAt, "+
                            "r.report_result AS reportResult "+
                            "FROM report r " +
                            "JOIN review v ON r.review_pk = v.review_pk "+
                            "WHERE r.report_pk = :reportPk",nativeQuery = true
                            )
    GetReportOneResForAdmin findReportOneByReportPkForAdmin(Long reportPk);


    @Query(value = "SELECT   r.report_title AS reportTitle, "+
                            "r.report_content AS reportContent, "+
                            "r.created_at AS reportCreatedAt, "+
                            "r.updated_at AS reportUpdatedAt, "+
                            "v.review_contents AS reviewContents, "+
                            "v.review_pics_1 AS reviewPics1, "+
                            "v.review_pics_2 AS reviewPics2, "+
                            "v.review_pics_3 AS reviewPics3, "+
                            "v.review_pics_4 AS reviewPics4, "+
                            "(SELECT user_nickname FROM user WHERE user_pk = v.user_pk) AS reviewNickName, "+
                            "r.report_state AS reportState, "+
                            "v.created_at AS reviewCreatedAt, "+
                            "r.report_result AS reportResult "+
                            "FROM report r " +
                            "JOIN review v ON r.review_pk = v.review_pk "+
                            "WHERE r.report_pk = :reportPk",nativeQuery = true
    )
    GetReportOneResForUser findReportOneForUser(Long reportPk);
}
