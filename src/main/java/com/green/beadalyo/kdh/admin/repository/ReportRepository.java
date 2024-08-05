package com.green.beadalyo.kdh.admin.repository;

import com.green.beadalyo.kdh.admin.entity.ReportEntity;
import com.green.beadalyo.kdh.admin.report.model.GetReportListResInterface;
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
                    "FROM report r",nativeQuery = true)
    List<GetReportListResInterface> findReportList();

    @Query(value = "SELECT r.report_pk AS reportPK,"+
            "r.report_title AS reportTitle,"+
            "r.report_state AS reportState,"+
            "r.updated_at AS updatedAt,"+
            "(SELECT user_nickname "+
            "FROM user"+
            "WHERE user_pk = r.user_pk) AS reportUserNickname,"+
            "r.created_at AS createdAt"+
            "FROM report r"+
            "where r.report_state = 2",nativeQuery = true)
    List<GetReportListResInterface> findReportFinishedList();

    @Query(value = "SELECT r.report_pk AS reportPK,"+
            "r.report_title AS reportTitle,"+
            "r.report_state AS reportState,"+
            "r.updated_at AS updatedAt,"+
            "(SELECT user_nickname "+
            "FROM user"+
            "WHERE user_pk = r.user_pk) AS reportUserNickname,"+
            "r.created_at AS createdAt"+
            "FROM report r"+
            "where r.report_state = 1",nativeQuery = true)
    List<GetReportListResInterface> findReportUnFinishedList();
}
