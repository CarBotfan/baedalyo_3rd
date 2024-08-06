package com.green.beadalyo.kdh.report.user;

import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.report.ReportRepository;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import com.green.beadalyo.kdh.report.user.model.GetReportListResForUser;
import com.green.beadalyo.kdh.report.user.model.GetReportOneResForUser;
import com.green.beadalyo.kdh.report.user.model.ReportPutReq;
import com.green.beadalyo.lhn.Review.ReviewRepository;
import com.green.beadalyo.lhn.Review.entity.Review;
import com.green.beadalyo.kdh.report.user.model.ReportPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceForUser {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public Review getReviewByPk(Long reviewPk){
       return reviewRepository.getReferenceById(reviewPk);
    }

    public ReportEntity makeReportForPost(ReportPostReq req) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setUserPk(req.getUser());
        reportEntity.setReviewPk(req.getReview());
        reportEntity.setReportContent(req.getReportContent());
        reportEntity.setReportTitle(req.getReportTitle());
        reportEntity.setReportState(1);
        return reportEntity;
    }

    public ReportEntity makeReportForPut(ReportPutReq req) {

        ReportEntity reportEntity = reportRepository.getReferenceById(req.getReportPk());
        if ( req.getUser() != reportEntity.getUserPk()){
            throw new RuntimeException();
        }
        reportEntity.setReportContent(req.getReportContent());
        reportEntity.setReportTitle(req.getReportTitle());
        return reportEntity;
    }

    public Integer saveReport(ReportEntity report){
        reportRepository.save(report);
        return 1;
    }

    public List<GetReportListResForUser> getReportListForUsers(Long userPk){
       return reportRepository.findReportListForUser(userPk);
    }

    public GetReportOneResForUser getReportOneForUser(Long reportPk){
        return reportRepository.findReportOneForUser(reportPk);
    }

    public void delReport(Long reportPk){
        ReportEntity reportEntity = reportRepository.getReferenceById(reportPk);
        if (reportEntity.getReportState() == 2){
            throw new RuntimeException();
        }
        reportRepository.delete(reportEntity);
    }
}
