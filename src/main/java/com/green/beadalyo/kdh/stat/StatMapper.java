package com.green.beadalyo.kdh.stat;

import com.green.beadalyo.kdh.stat.model.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatMapper {
    GetReviewCountRes getReviewCount(GetReviewStatReq p);
    GetReviewAvgRes getReviewAvg(GetReviewStatReq p);
    GetMonthSaleRes getMonthSales(GetDateReq p);

    GetMonthOrderCountRes getMonthOrderCount(GetDateReq p);

    GetDailySalesRes getDailySales(GetDateReq p);

    GetDailyOrderCountRes getDailyOrderCount(GetDateReq p);
}
