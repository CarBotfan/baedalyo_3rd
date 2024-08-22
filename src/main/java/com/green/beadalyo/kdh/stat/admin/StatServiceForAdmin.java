package com.green.beadalyo.kdh.stat.admin;

import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.stat.admin.model.*;
import com.green.beadalyo.kdh.stat.owner.model.GetMonthSaleRes;
import com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.DailySalesDto;
import com.green.beadalyo.lmy.doneorder.model.MonthOrderCountDto;
import com.green.beadalyo.lmy.doneorder.model.MonthSalesDto;
import com.green.beadalyo.lmy.doneorder.repository.DoneOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceForAdmin {
    private final RestaurantRepository restaurantRepository;
    private final DoneOrderRepository doneOrderRepository;
    private final UserRepository userRepository;
    public boolean checkRestaurant(Long resPk){
//        Restaurant restaurant = restaurantRepository.getReferenceById(resPk);
//        if (restaurant == null){
//            return false;
//        }
//        return true;
       return restaurantRepository.existsById(resPk);
    }


    public List<GetDailySignUpCount> getDailySignUpCount(String date){
        return userRepository.getDailySignUpCount(date);
    }

    public List<GetMonthSignUpCount> getMonthSignUpCount(String date){
        return userRepository.getMonthSignUpCount(date);
    }

    public List<GetDailySignOutCount> getDailySignOutCount(String date){
        return userRepository.getDailySignOutCount(date);
    }

    public List<GetMonthSignOutCount> getMonthSignOutCount(String date){
        return userRepository.getMonthSignOutCount(date);
    }

    public List<MonthSalesDto> getMonthSaleByAllRes(String date){
        return doneOrderRepository.getMonthSalesByAllRes(date);
    }

    public List<MonthOrderCountDto> getMonthOrderCountByAllRes(String date){
        return doneOrderRepository.getMonthOrderCountByAllRes(date);
    }

    public List<DailySalesDto> getDailySalesByAllRes(String date){
        return doneOrderRepository.getDailySalesByAllRes(date);
    }

    public List<DailyOrderCountDto> getDailyOrderCountByAllRes(String date){
        return doneOrderRepository.getDailyOrderCountByAllRes(date);
    }
}
