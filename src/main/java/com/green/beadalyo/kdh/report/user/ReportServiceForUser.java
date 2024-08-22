package com.green.beadalyo.kdh.report.user;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.exception.DuplicatedIdException;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.report.ReportRepository;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import com.green.beadalyo.kdh.report.user.model.*;
import com.green.beadalyo.lhn.Review.ReviewRepository;
import com.green.beadalyo.lhn.Review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceForUser {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final AuthenticationFacade authenticationFacade;

    public Integer getReportCountByReviewPk(Review review) {
        return reportRepository.countByReviewPk(review);
    }

    public User getUserByPk(Long userPk){
        return userRepository.getReferenceById(userPk);
    }

    public Review getReviewByPk(Long reviewPk){
       return reviewRepository.getReferenceById(reviewPk);
    }

    public ReportEntity makeReportForPost(PostReportForUserReq req) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setUser(req.getUser());
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

    public Page<ReportEntity> getReportListForUsers(Long userPk, Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        User user = getUserByPk(userPk);
       return reportRepository.findByUserOrderByReportPkDesc(user, pageable);
    }

    public List<GetReportListResForUser> makeGetReportListForUser(Page<ReportEntity> pageEntity){
        List<GetReportListResForUser> result = new ArrayList<>();
        for (ReportEntity report : pageEntity){
            GetReportListResForUser getReportListResForUser = new GetReportListResForUser(
                    report.getReportPk(),
                    report.getReportTitle(),
                    report.getReportState(),
                    report.getUpdatedAt(),
                    report.getCreatedAt()
            );
            result.add(getReportListResForUser);
        }
        return  result;
    }

    public ReportListDto makeReportListDto(ReportListDto reportListDto, Page<ReportEntity> pageEntity,
                                           List<GetReportListResForUser> resultList){
        reportListDto.setTotalPage(pageEntity.getTotalPages());
        reportListDto.setTotalElements(pageEntity.getNumberOfElements());
        reportListDto.setResult(resultList);
        return reportListDto;
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
        if (reportEntity.getUser().getUserPk() != userPk){
            return false;
        }
        return true;
    }
}
