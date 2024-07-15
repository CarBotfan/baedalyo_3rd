package com.green.beadalyo.kdh.stat;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.stat.model.*;
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
    private final AuthenticationFacade authenticationFacade;


    public GetReviewCountRes getReviewCount(GetReviewStatReq p){
        return mapper.getReviewCount(p);
    }

    public GetReviewAvgRes getReviewAvg(GetReviewStatReq p){
        return mapper.getReviewAvg(p);
    }

    public List<GetMonthSaleRes> getMonthSales(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        log.info("resUserPk: {}", p.getResUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        log.info("resPk: {}", resPk);
        p.setResPk(resPk);
        log.info("p.getResPk: {}",p.getResPk());
        if (resPk == 0 ){
            throw new RuntimeException();
        }
        List<GetMonthSaleRes> list = mapper.getMonthSales(p);
        return list;
    }

    public List<GetMonthOrderCountRes> getMonthOrderCount(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        p.setResPk(resPk);
        if (resPk == null || resPk == 0 ){
            throw new RuntimeException();
        }
        List<GetMonthOrderCountRes> list = mapper.getMonthOrderCount(p);
        return list;
    }

    public List<GetDailySalesRes> getDailySales(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());
        p.setResPk(resPk);
        if (resPk == null || resPk == 0 ){
            throw new RuntimeException();
        }
        List<GetDailySalesRes> list = mapper.getDailySales(p);
        return list;
    }

    public List<GetDailyOrderCountRes> getDailyOrderCount(GetDateReq p){
        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkResPk(p.getResUserPk());

        p.setResPk(resPk);
        if (resPk == null || resPk == 0 ){
            throw new RuntimeException();
        }

        List<GetDailyOrderCountRes> list = mapper.getDailyOrderCount(p);

        return list;
    }
}
