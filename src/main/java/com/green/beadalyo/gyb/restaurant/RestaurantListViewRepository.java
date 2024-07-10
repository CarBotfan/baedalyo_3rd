package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.RestaurantListView;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface RestaurantListViewRepository extends JpaRepository<RestaurantListView, Long>
{
    @Query("SELECT r FROM RestaurantListView r JOIN MatchingCategoryRestaurant m ON r.restaurantPk = m.restaurant.seq " +
            "WHERE m.category.seq = :categoryId " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax order by r.createdAt asc")
    Page<RestaurantListView> findByCategoryIdAndCoordinates(@Param("categoryId") Long categoryId,
                                                            @Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                            Pageable pageable);

    @Query(value = "SELECT r.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX)))) AS distance " +
            "FROM restaurant_list_view r " +
            "WHERE r.restaurantPk IN (SELECT a.restaurantPk FROM restaurant_list_view a JOIN cate_res_matching crm ON a.restaurantPk = crm.crm_res_pk WHERE crm.crm_cate_pk = :categoryId) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY distance",
            countQuery = "SELECT count(*) " +
                    "FROM restaurant_list_view r " +
                    "WHERE r.restaurantPk IN (SELECT a.restaurantPk FROM restaurant_list_view a JOIN cate_res_matching crm ON a.restaurantPk = crm.crm_res_pk WHERE crm.crm_cate_pk = :categoryId) " +
                    "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
                    "AND r.restaurantCoorY BETWEEN :yMin AND :yMax",
            nativeQuery = true)
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByDistance(@Param("categoryId") Long categoryId,
                                                                            @Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                            Pageable pageable);

    @Query("SELECT r FROM RestaurantListView r JOIN MatchingCategoryRestaurant m ON r.restaurantPk = m.restaurant.seq " +
            "WHERE m.category.seq = :categoryId " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY r.reviewAvgScore DESC")    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
                                                                         @Param("xMin") BigDecimal xMin,
                                                                         @Param("xMax") BigDecimal xMax,
                                                                         @Param("yMin") BigDecimal yMin,
                                                                         @Param("yMax") BigDecimal yMax,
                                                                         Pageable pageable);

}
