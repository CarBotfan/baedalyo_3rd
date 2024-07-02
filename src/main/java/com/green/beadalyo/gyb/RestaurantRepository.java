package com.green.beadalyo.gyb;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>
{

    Optional<Restaurant> findTop1ByUser(User user);
    Optional<Restaurant> findTop1BySeq(Long seq);
}
