package com.green.beadalyo.kdh.report.admin;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.report.admin.model.GetReportListResForAdmin;
import com.green.beadalyo.kdh.report.admin.model.GetReportOneResForAdmin;
import com.green.beadalyo.kdh.report.admin.model.PatchAccountSuspensionReq;
import com.green.beadalyo.kdh.report.ReportRepository;
import com.green.beadalyo.kdh.report.entity.ReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceForAdmin {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public List<GetReportListResForAdmin> getReportList(){
        return reportRepository.findReportListForAdmin();
    }

    public List<GetReportListResForAdmin> getReportFinishedList(){
        return reportRepository.findReportFinishedListForAdmin();
    }

    public List<GetReportListResForAdmin> getReportUnFinishedList(){
        return reportRepository.findReportUnFinishedListForAdmin();
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
}
