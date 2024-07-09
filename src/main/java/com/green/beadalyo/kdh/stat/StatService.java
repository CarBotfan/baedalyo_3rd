package com.green.beadalyo.kdh.stat;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.stat.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatMapper mapper;
    private final AuthenticationFacade authenticationFacade;


    public GetReviewCountRes getReviewCount(GetReviewStatReq p){
        return mapper.getReviewCount(p);
    }

    public GetReviewAvgRes getReviewAvg(GetReviewStatReq p){
        return mapper.getReviewAvg(p);
    }

    public GetMonthSaleRes getMonthSales(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());

        Long result = mapper.checkResPk(p);
        if (result == null || result == 0 ){
            throw new RuntimeException("잘못된 접근입니다.");
        }
        return mapper.getMonthSales(p);
    }

    public GetMonthOrderCountRes getMonthOrderCount(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());

        Long result = mapper.checkResPk(p);
        if (result == null || result == 0 ){
            throw new RuntimeException("잘못된 접근입니다.");
        }
        return mapper.getMonthOrderCount(p);
    }

    public GetDailySalesRes getDailySales(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());

        Long result = mapper.checkResPk(p);
        if (result == null || result == 0 ){
            throw new RuntimeException("잘못된 접근입니다.");
        }
        return mapper.getDailySales(p);
    }

    public GetDailyOrderCountRes getDailyOrderCount(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());

        Long result = mapper.checkResPk(p);
        if (result == null || result == 0 ){
            throw new RuntimeException("잘못된 접근입니다.");
        }
        return mapper.getDailyOrderCount(p);
    }
}
