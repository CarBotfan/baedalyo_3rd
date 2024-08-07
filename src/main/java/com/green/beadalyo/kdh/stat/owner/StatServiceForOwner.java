package com.green.beadalyo.kdh.stat.owner;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.stat.owner.model.GetDateReq;
import com.green.beadalyo.kdh.stat.owner.model.GetReviewAvgRes;
import com.green.beadalyo.kdh.stat.owner.model.GetReviewCountRes;
import com.green.beadalyo.kdh.stat.owner.model.GetReviewStatReq;
import com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.DailySalesDto;
import com.green.beadalyo.lmy.doneorder.model.MonthOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.MonthSalesDto;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceForOwner {
    private final StatMapper mapper;
    private final DoneOrderRepository doneOrderRepository;
    private final AuthenticationFacade authenticationFacade;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    public GetReviewCountRes getReviewCount(GetReviewStatReq p){
        return mapper.getReviewCount(p);
    }

    public GetReviewAvgRes getReviewAvg(GetReviewStatReq p){
        return mapper.getReviewAvg(p);
    }

    public List<MonthSalesDto> getMonthSales(GetDateReq p){
        long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        if (restaurant == null){
            throw new RuntimeException();
        }
        List<MonthSalesDto> list = doneOrderRepository.getMonthSales(p.getDate(), restaurant.getSeq());
        return list;
    }

    public List<MonthOrderCountDto> getMonthOrderCount(GetDateReq p){
        long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        if (restaurant == null){
            throw new RuntimeException();
        }
        List<MonthOrderCountDto> list = doneOrderRepository.getMonthOrderCount(p.getDate(),restaurant.getSeq());
        return list;
    }

    public List<DailySalesDto> getDailySales(GetDateReq p){
        long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        if (restaurant == null){
            throw new RuntimeException();
        }
        List<DailySalesDto> list = doneOrderRepository.getDailySales(p.getDate(), restaurant.getSeq());
        return list;
    }

    public List<DailyOrderCountDto> getDailyOrderCount(GetDateReq p){
        long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        if (restaurant == null){
            throw new RuntimeException();
        }
        List<DailyOrderCountDto> list = doneOrderRepository.getDailyOrderCount(p.getDate(),restaurant.getSeq());
        return list;
    }
}
