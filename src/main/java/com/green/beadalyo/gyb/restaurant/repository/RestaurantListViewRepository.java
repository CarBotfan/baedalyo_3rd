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
    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.createdAt ASC, r.restaurantState ASC")
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
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.createdAt ASC, r.restaurantState ASC")
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
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.createdAt ASC, r.restaurantState ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinates(@Param("xMin") BigDecimal xMin,
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
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.createdAt ASC, r.restaurantState ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinates(@Param("xMin") BigDecimal xMin,
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
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX)))), r.restaurantState ASC"
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
            "left JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX)))), r.restaurantState ASC"
    )
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByDistance(@Param("categoryId") Long categoryId,
                                                                            @Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                            @Param("userPk") Long userPk,
                                                                            Pageable pageable);





    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX)))), r.restaurantState ASC"
    )
    Page<RestaurantListView> findALLByCategoryIdAndCoordinatesSortedByDistance(@Param("xMin") BigDecimal xMin,
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
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq  AND rf.userPk.userPk = :userPk " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX)))), r.restaurantState ASC")
    Page<RestaurantListView> findALLByCategoryIdAndCoordinatesSortedByDistance(@Param("xMin") BigDecimal xMin,
                                                                               @Param("xMax") BigDecimal xMax,
                                                                               @Param("yMin") BigDecimal yMin,
                                                                               @Param("yMax") BigDecimal yMax,
                                                                               @Param("latitude") BigDecimal latitude,
                                                                               @Param("longitude") BigDecimal longitude,
                                                                               @Param("userPk") Long userPk,
                                                                               Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.reviewAvgScore DESC, r.restaurantState ASC")
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
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
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.reviewAvgScore DESC, r.restaurantState ASC")
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
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
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.reviewAvgScore DESC, r.restaurantState ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinatesSortedByScore(@Param("xMin") BigDecimal xMin,
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
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY r.reviewAvgScore DESC, r.restaurantState ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinatesSortedByScore(@Param("xMin") BigDecimal xMin,
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
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "and rf.resFollowPk is not NULL " +
            "AND r.restaurantState IN (1, 2) " +
            "ORDER BY rf.createdAt, r.restaurantState ASC")
    Page<RestaurantListView> findFollowedRestaurant(@Param("categoryId") Long categoryId,
                                                    @Param("xMin") BigDecimal xMin,
                                                    @Param("xMax") BigDecimal xMax,
                                                    @Param("yMin") BigDecimal yMin,
                                                    @Param("yMax") BigDecimal yMax,
                                                    @Param("userPk") Long userPk,
                                                    Pageable pageable);
}
