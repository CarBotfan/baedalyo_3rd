package com.green.beadalyo.kdh.accept;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.kdh.accept.model.GetUnAcceptRestaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AcceptService {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getUnAcceptRestaurant(){
       return restaurantRepository.findByState(3);
    }

    public Integer acceptRestaurant(Long resPk){
        Restaurant restaurant = restaurantRepository.getReferenceById(resPk);
        restaurant.setState(2);
        restaurantRepository.save(restaurant);
        return 1;
    }
}
