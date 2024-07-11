package com.green.beadalyo.kdh.stat;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.stat.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("resUserPk: {}", p.getResUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        log.info("resPk: {}", resPk);
        p.setResPk(resPk);
        log.info("p.getResPk: {}",p.getResPk());
        if (resPk == 0 ){
            throw new RuntimeException();
        }
        return mapper.getMonthSales(p);
    }

    public GetMonthOrderCountRes getMonthOrderCount(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        p.setResPk(resPk);
        if (resPk == null || resPk == 0 ){
            throw new RuntimeException();
        }
        return mapper.getMonthOrderCount(p);
    }

    public GetDailySalesRes getDailySales(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        p.setResPk(resPk);
        if (resPk == null || resPk == 0 ){
            throw new RuntimeException();
        }
        return mapper.getDailySales(p);
    }

    public GetDailyOrderCountRes getDailyOrderCount(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        p.setResPk(resPk);
        if (resPk == null || resPk == 0 ){
            throw new RuntimeException();
        }
        return mapper.getDailyOrderCount(p);
    }
}
