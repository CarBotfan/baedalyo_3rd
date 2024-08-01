package com.green.beadalyo.kdh.stat;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.stat.model.*;
import com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.DailySalesDto;
import com.green.beadalyo.lmy.doneorder.model.MonthOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.MonthSalesDto;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {
    private final StatMapper mapper;
    private final DoneOrderRepository doneOrderRepository;


    public GetReviewCountRes getReviewCount(GetReviewStatReq p){
        return mapper.getReviewCount(p);
    }

    public GetReviewAvgRes getReviewAvg(GetReviewStatReq p){
        return mapper.getReviewAvg(p);
    }

    public List<MonthSalesDto> getMonthSales(GetDateReq p){

        List<MonthSalesDto> list = doneOrderRepository.getMonthSales(p.getDate(), p.getResPk());
        return list;
    }

    public List<MonthOrderCountDto> getMonthOrderCount(GetDateReq p){
        List<MonthOrderCountDto> list = doneOrderRepository.getMonthOrderCount(p.getDate(),p.getResPk());
        return list;
    }

    public List<DailySalesDto> getDailySales(GetDateReq p){
        List<DailySalesDto> list = doneOrderRepository.getDailySales(p.getDate(), p.getResPk());
        return list;
    }

    public List<DailyOrderCountDto> getDailyOrderCount(GetDateReq p){
        List<DailyOrderCountDto> list = doneOrderRepository.getDailyOrderCount(p.getDate(),p.getResPk());
        return list;
    }
}
