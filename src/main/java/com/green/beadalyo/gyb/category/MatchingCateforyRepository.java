package com.green.beadalyo.gyb.category;

import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.MatchingCategoryRestaurant;
import com.green.beadalyo.gyb.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingCateforyRepository extends JpaRepository<MatchingCategoryRestaurant, Long>
{

    MatchingCategoryRestaurant findTop1ByCategoryAndRestaurant(Category category, Restaurant restaurant);
}
