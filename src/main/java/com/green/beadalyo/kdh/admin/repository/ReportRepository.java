package com.green.beadalyo.kdh.admin.repository;

import com.green.beadalyo.kdh.admin.entity.ReportEntity;
import com.green.beadalyo.kdh.admin.report.model.GetReportListResInterface;
import com.green.beadalyo.kdh.admin.report.model.GetReportOneResInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    @Query(value = "SELECT r.report_pk AS reportPK,"+
                    "r.report_title AS reportTitle,"+
                    "r.report_state AS reportState,"+
                    "r.updated_at AS updatedAt,"+
                    "(SELECT user_nickname "+
                    "FROM user"+
                    "WHERE user_pk = r.user_pk) AS reportUserNickname,"+
                    "r.created_at AS createdAt"+
                    "FROM report r"+
                    "ORDER BY r.report_pk DESC",nativeQuery = true)
    List<GetReportListResInterface> findReportList();

    @Query(value = "SELECT r.report_pk AS reportPK,"+
            "r.report_title AS reportTitle,"+
            "r.report_state AS reportState,"+
            "r.updated_at AS updatedAt,"+
            "(SELECT user_nickname FROM user WHERE user_pk = r.user_pk) AS reportUserNickname,"+
            "r.created_at AS createdAt"+
            "FROM report r"+
            "where r.report_state = 2"+
            "ORDER BY r.report_pk DESC",nativeQuery = true)
    List<GetReportListResInterface> findReportFinishedList();

    @Query(value = "SELECT r.report_pk AS reportPK,"+
                    "r.report_title AS reportTitle,"+
                    "r.report_state AS reportState,"+
                    "r.updated_at AS updatedAt,"+
                    "(SELECT user_nickname FROM user WHERE user_pk = r.user_pk) AS reportUserNickname,"+
                    "r.created_at AS createdAt"+
                    "FROM report r"+
                    "where r.report_state = 1"+
                    "ORDER BY r.report_pk DESC",nativeQuery = true)
    List<GetReportListResInterface> findReportUnFinishedList();

    @Query(value = "SELECT   r.report_title AS reportTitle,"+
                            "r.report_content AS reportContent,"+
                            "(SELECT user_nickname FROM user WHERE user_pk = r.user_pk) AS reportNickName,"+
                            "r.created_at AS reportCreatedAt,"+
                            "v.review_contents AS reviewContents,"+
                            "v.review_pics_1 AS reviewPics1,"+
                            "v.review_pics_2 AS reviewPics2,"+
                            "v.review_pics_3 AS reviewPics3,"+
                            "v.review_pics_4 AS reviewPics4,"+
                            "(SELECT user_nickname FROM user WHERE user_pk = v.user_pk) AS reviewNickName,"+
                            "v.created_at AS reviewCreatedAt"+
                            "FROM report r" +
                            "JOIN review v ON r.review_pk = v.review_pk"+
                            "WHERE r.report_pk = :reportPk",nativeQuery = true
                            )
    GetReportOneResInterface findReportOneByReportPk(Long reportPk);
}
