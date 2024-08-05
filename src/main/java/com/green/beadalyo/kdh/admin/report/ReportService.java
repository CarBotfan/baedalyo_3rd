package com.green.beadalyo.kdh.admin.report;

import com.green.beadalyo.kdh.admin.report.model.GetReportListResInterface;
import com.green.beadalyo.kdh.admin.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public List<GetReportListResInterface> getReportList(){
        return reportRepository.findReportList();
    }

    public List<GetReportListResInterface> getReportFinishedList(){
        return reportRepository.findReportFinishedList();
    }

    public List<GetReportListResInterface> getReportUnFinishedList(){
        return reportRepository.findReportUnFinishedList();
    }
}
