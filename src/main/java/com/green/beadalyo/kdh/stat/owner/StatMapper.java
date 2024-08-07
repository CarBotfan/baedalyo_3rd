package com.green.beadalyo.kdh.stat.owner;

import com.green.beadalyo.kdh.stat.model.*;
import com.green.beadalyo.kdh.stat.owner.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatMapper {
    GetReviewCountRes getReviewCount(GetReviewStatReq p);

    GetReviewAvgRes getReviewAvg(GetReviewStatReq p);

    List<GetMonthSaleRes> getMonthSales(GetDateReq p);

    List<GetMonthOrderCountRes> getMonthOrderCount(GetDateReq p);

    List<GetDailySalesRes> getDailySales(GetDateReq p);

    List<GetDailyOrderCountRes> getDailyOrderCount(GetDateReq p);

    Long checkResPk(long resUserPk);
}
