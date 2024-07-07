package com.green.beadalyo.kdh.stat;

import com.green.beadalyo.kdh.stat.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatMapper mapper;

    public GetReviewCountRes getReviewCount(GetReviewStatReq p){
        return mapper.getReviewCount(p);
    }

    public GetReviewAvgRes getReviewAvg(GetReviewStatReq p){
        return mapper.getReviewAvg(p);
    }

    public GetMonthSaleRes getMonthSales(GetDateReq p){
        return mapper.getMonthSales(p);
    }

    public GetMonthOrderCountRes getMonthOrderCount(GetDateReq p){
        return mapper.getMonthOrderCount(p);
    }

    public GetDailySalesRes getDailySales(GetDateReq p){
        return mapper.getDailySales(p);
    }

    public GetDailyOrderCountRes getDailyOrderCount(GetDateReq p){
        return mapper.getDailyOrderCount(p);
    }
}
