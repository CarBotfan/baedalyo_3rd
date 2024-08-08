package com.green.beadalyo.gyb.restaurant.repository;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.accept.model.GetUnAcceptRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>
{

    Optional<Restaurant> findTop1ByUser(User user);
    Optional<Restaurant> findTop1BySeq(Long seq);

    Restaurant findRestaurantByUser(User user);
    Restaurant findRestaurantBySeq(Long seq);

//    @Query(value = "SELECT res_pk AS resPk, " +
//                        "res_user_pk AS resUserPk, " +
//                        "res_name AS resName, " +
//                        "res_regi_num AS resRegiNum, " +
//                        "res_addr AS resAddr, " +
//                        "res_description1 AS resDescription1, " +
//                        "res_description2 AS resDescription2, " +
//                        "created_at AS createdAt " +
//                        "FROM restaurant " +
//                        "WHERE res_state = 3",
//                        nativeQuery = true)
    List<Restaurant> findByState(Integer state);


}

