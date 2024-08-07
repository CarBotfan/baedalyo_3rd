package com.green.beadalyo.gyb.restaurant.repository;

import com.green.beadalyo.gyb.model.RestaurantListView;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface RestaurantListViewRepository extends JpaRepository<RestaurantListView, Long>
{
    @Query("SELECT r FROM RestaurantListView r " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY r.createdAt ASC")
    Page<RestaurantListView> findByCategoryIdAndCoordinates(@Param("categoryId") Long categoryId,
                                                            @Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                            Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY r.createdAt ASC")
    Page<RestaurantListView> findByCategoryIdAndCoordinates(@Param("categoryId") Long categoryId,
                                                            @Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                            @Param("userPk") Long userPk,
                                                            Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "join ResFollow rf on r.restaurantPk = rf.resPk.seq  AND rf.userPk.userPk = :userPk " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX))))"
    )
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByDistance(@Param("categoryId") Long categoryId,
                                                                            @Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                            Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "and rf.resFollowPk is not NULL " +
            "ORDER BY rf.createdAt")
    Page<RestaurantListView> findFollowedRestaurant(@Param("categoryId") Long categoryId,
                                                                            @Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("userPk") Long userPk,
                                                                            Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "join ResFollow rf on r.restaurantPk = rf.resPk.seq  AND rf.userPk.userPk = :userPk " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX))))"
    )
    Page<RestaurantListView> findALLByCategoryIdAndCoordinatesSortedByDistance(@Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                            Pageable pageable);


    @Query(value = "SELECT r.*, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurant_coor_x)) * cos(radians(r.restaurant_coor_y) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurant_coor_x)))) AS distance, " +
            "CASE WHEN rf.res_follow_pk IS NOT NULL THEN 1 ELSE 0 END AS is_follow " +
            "FROM restaurant_list_view r " +
            "LEFT JOIN res_follow rf ON r.restaurant_pk = rf.res_pk AND rf.user_pk = :userPk " +
            "WHERE r.restaurant_coor_x BETWEEN :xMin AND :xMax " +
            "AND r.restaurant_coor_y BETWEEN :yMin AND :yMax " +
            "ORDER BY distance",
            countQuery = "SELECT count(*) " +
                    "FROM restaurant_list_view r " +
                    "LEFT JOIN res_follow rf ON r.restaurant_pk = rf.res_pk AND rf.user_pk = :userPk " +
                    "WHERE r.restaurant_coor_x BETWEEN :xMin AND :xMax " +
                    "AND r.restaurant_coor_y BETWEEN :yMin AND :yMax",
            nativeQuery = true)
    Page<RestaurantListView> findALLByCategoryIdAndCoordinatesSortedByDistance(@Param("xMin") BigDecimal xMin,
                                                                               @Param("xMax") BigDecimal xMax,
                                                                               @Param("yMin") BigDecimal yMin,
                                                                               @Param("yMax") BigDecimal yMax,
                                                                               @Param("latitude") BigDecimal latitude,
                                                                               @Param("longitude") BigDecimal longitude,
                                                                               @Param("userPk") Long userPk,
                                                                               Pageable pageable);


    @Query("SELECT r FROM RestaurantListView r " +
            "LEFT JOIN MatchingCategoryRestaurant m ON r.restaurantPk = m.restaurant.seq AND m.category.seq = :categoryId " +
            "WHERE (:categoryId = 0 OR m.category.seq = :categoryId) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "ORDER BY r.reviewAvgScore DESC")
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
                                                                         @Param("xMin") BigDecimal xMin,
                                                                         @Param("xMax") BigDecimal xMax,
                                                                         @Param("yMin") BigDecimal yMin,
                                                                         @Param("yMax") BigDecimal yMax,
                                                                         Pageable pageable);


    @Query(value = "SELECT r.*, " +
            "CASE WHEN rf.res_follow_pk IS NOT NULL THEN 1 ELSE 0 END AS isFollow " +
            "FROM restaurant_list_view r " +
            "LEFT JOIN matching_category_restaurant m ON r.restaurant_pk = m.restaurant_seq AND m.category_seq = :categoryId " +
            "LEFT JOIN res_follow rf ON r.restaurant_pk = rf.res_pk AND rf.user_pk = :userPk " +
            "WHERE (:categoryId = 0 OR m.category_seq = :categoryId) " +
            "AND r.restaurant_coor_x BETWEEN :xMin AND :xMax " +
            "AND r.restaurant_coor_y BETWEEN :yMin AND :yMax " +
            "ORDER BY r.review_avg_score DESC",
            countQuery = "SELECT count(*) " +
                    "FROM restaurant_list_view r " +
                    "LEFT JOIN matching_category_restaurant m ON r.restaurant_pk = m.restaurant_seq AND m.category_seq = :categoryId " +
                    "LEFT JOIN res_follow rf ON r.restaurant_pk = rf.res_pk AND rf.user_pk = :userPk " +
                    "WHERE (:categoryId = 0 OR m.category_seq = :categoryId) " +
                    "AND r.restaurant_coor_x BETWEEN :xMin AND :xMax " +
                    "AND r.restaurant_coor_y BETWEEN :yMin AND :yMax",
            nativeQuery = true)
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
                                                                         @Param("xMin") BigDecimal xMin,
                                                                         @Param("xMax") BigDecimal xMax,
                                                                         @Param("yMin") BigDecimal yMin,
                                                                         @Param("yMax") BigDecimal yMax,
                                                                         @Param("userPk") Long userPk,
                                                                         Pageable pageable);
}
