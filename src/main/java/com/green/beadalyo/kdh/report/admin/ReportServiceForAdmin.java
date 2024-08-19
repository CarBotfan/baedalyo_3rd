package com.green.beadalyo.kdh.report.admin;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.report.admin.model.*;
import com.green.beadalyo.kdh.report.ReportRepository;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceForAdmin {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public Page<ReportEntity> getReportList(Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        return reportRepository.findAllByOrderByReportPkDesc(pageable);
    }

    public List<GetReportListResForAdmin> makeGetReportListForAdmin(Page<ReportEntity> pageEntity){
        List<GetReportListResForAdmin> result = new ArrayList<>();
        for (ReportEntity report : pageEntity){
            GetReportListResForAdmin getReportListResForAdmin = new GetReportListResForAdmin(
                    report.getReportPk(),
                    report.getReportTitle(),
                    report.getReportState(),
                    report.getUpdatedAt(),
                    report.getUser().getUserNickname(),
                    report.getCreatedAt()
            );
            result.add(getReportListResForAdmin);
        }
        return result;
    }

    public ReportListDto makeReportListDto(ReportListDto reportListDto, Page<ReportEntity> pageEntity,
                                           List<GetReportListResForAdmin> resultList){
        reportListDto.setTotalPage(pageEntity.getTotalPages());
        reportListDto.setTotalElements(pageEntity.getNumberOfElements());
        reportListDto.setResult(resultList);
        return reportListDto;
    }

    public Page<ReportEntity> getReportFinishedList(Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        return reportRepository.findByReportStateOrderByReportPkDesc(2,pageable);
    }

    public Page<ReportEntity> getReportUnFinishedList(Integer page){
        Pageable pageable = PageRequest.of(page-1, 10);
        return reportRepository.findByReportStateOrderByReportPkDesc(1, pageable);
    }

    public GetReportOneResForAdmin getReportOne(Long reportPk){
        return reportRepository.findReportOneByReportPkForAdmin(reportPk);
    }


    public void patchUserStateAndBlockDate(PatchAccountSuspensionReq p){
        User user = userRepository.getReferenceById(p.getUserPk());
        user.setUserState(2);
        user.setUserBlockDate(LocalDate.now().plus(p.getUserBlockDate(), ChronoUnit.DAYS));
        userRepository.save(user);
    }

    public void patchReportStateAndResult(PatchAccountSuspensionReq p){
        ReportEntity reportEntity = reportRepository.getReferenceById(p.getReportPk());
        reportEntity.setReportState(2);
        reportEntity.setReportResult(String.format("%d일 정지",p.getUserBlockDate()));
        reportRepository.save(reportEntity);
    }

    public void delReport(Long reportPk){
        ReportEntity reportEntity = reportRepository.getReferenceById(reportPk);
        reportRepository.delete(reportEntity);
    }

//    public ReportEntity makePutReportEntity(PutReportForAdminReq p){
//        ReportEntity reportEntity = reportRepository.getReferenceById(p.getReportPk());
//        reportEntity.setReportTitle(p.getReportTitle());
//        reportEntity.setReportContent(p.getReportContent());
//        return reportEntity;
//    }

    public void saveReport(ReportEntity report){
        reportRepository.save(report);
    }

}
