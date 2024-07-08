package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>
{

    Optional<Restaurant> findTop1ByUser(User user);
    Optional<Restaurant> findTop1BySeq(Long seq);

    @Query("SELECT r FROM Restaurant r JOIN r.categories c WHERE c.seq = :categoryId")
    Page<Restaurant> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
