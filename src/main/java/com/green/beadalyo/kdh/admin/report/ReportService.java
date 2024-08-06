package com.green.beadalyo.kdh.admin.report;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.admin.report.model.GetReportListResInterface;
import com.green.beadalyo.kdh.admin.report.model.GetReportOneResInterface;
import com.green.beadalyo.kdh.admin.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public List<GetReportListResInterface> getReportList(){
        return reportRepository.findReportList();
    }

    public List<GetReportListResInterface> getReportFinishedList(){
        return reportRepository.findReportFinishedList();
    }

    public List<GetReportListResInterface> getReportUnFinishedList(){
        return reportRepository.findReportUnFinishedList();
    }

    public GetReportOneResInterface getReportOne(Long reportPk){
        return reportRepository.findReportOneByReportPk(reportPk);
    }

    public User makeUserEntity(User user){

    }
}
