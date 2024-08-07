package com.green.beadalyo.kdh.report.user;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.exception.DuplicatedIdException;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.report.ReportRepository;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import com.green.beadalyo.kdh.report.user.model.GetReportListResForUser;
import com.green.beadalyo.kdh.report.user.model.GetReportOneResForUser;
import com.green.beadalyo.kdh.report.user.model.PutReportForUserReq;
import com.green.beadalyo.lhn.Review.ReviewRepository;
import com.green.beadalyo.lhn.Review.entity.Review;
import com.green.beadalyo.kdh.report.user.model.PostReportForUserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceForUser {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final AuthenticationFacade authenticationFacade;
    public Review getReviewByPk(Long reviewPk){
       return reviewRepository.getReferenceById(reviewPk);
    }

    public ReportEntity makeReportForPost(PostReportForUserReq req) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setUserPk(req.getUser());
        reportEntity.setReviewPk(req.getReview());
        reportEntity.setReportContent(req.getReportContent());
        reportEntity.setReportTitle(req.getReportTitle());
        reportEntity.setReportState(1);
        return reportEntity;
    }

    public ReportEntity makeReportForPut(PutReportForUserReq req) {
        ReportEntity reportEntity = reportRepository.getReferenceById(req.getReportPk());

        if (reportEntity.getReportState() == 2 ){
            throw new DuplicatedIdException();
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

    public boolean checkUser(Long reportPk){
        ReportEntity reportEntity = reportRepository.getReferenceById(reportPk);
        Long userPk = authenticationFacade.getLoginUserPk();
        if (reportEntity.getUserPk().getUserPk() != userPk){
            return false;
        }
        return true;
    }
}
