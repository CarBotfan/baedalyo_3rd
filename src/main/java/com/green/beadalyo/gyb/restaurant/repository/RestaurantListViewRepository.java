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
            "0, CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "where (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%) " +
            "group by r.restaurantPk " +
            "order BY r.restaurantState ASC, r.createdAt ASC")
    Page<RestaurantListView> findByCategoryIdAndCoordinates(@Param("categoryId") Long categoryId,
                                                            @Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                            @Param("search") String search,
                                                            Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "order BY r.restaurantState ASC, r.createdAt ASC")
    Page<RestaurantListView> findByCategoryIdAndCoordinates(@Param("categoryId") Long categoryId,
                                                            @Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                            @Param("search") String search,
                                                            @Param("userPk") Long userPk,
                                                            Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic,r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "order BY r.restaurantState ASC, r.createdAt ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinates(@Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                               @Param("search") String search,
                                                            Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "order BY r.restaurantState ASC, r.createdAt ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinates(@Param("xMin") BigDecimal xMin,
                                                            @Param("xMax") BigDecimal xMax,
                                                            @Param("yMin") BigDecimal yMin,
                                                            @Param("yMax") BigDecimal yMax,
                                                               @Param("search") String search,
                                                            @Param("userPk") Long userPk,
                                                            Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX)))), r.restaurantState ASC"
    )
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByDistance(@Param("categoryId") Long categoryId,
                                                                            @Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                            @Param("search") String search,
                                                                            Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "left JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC , (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX))))"
    )
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByDistance(@Param("categoryId") Long categoryId,
                                                                            @Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                            @Param("search") String search,
                                                                            @Param("userPk") Long userPk,
                                                                            Pageable pageable);





    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC , (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX))))"
    )
    Page<RestaurantListView> findALLByCategoryIdAndCoordinatesSortedByDistance(@Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("latitude") BigDecimal latitude,
                                                                            @Param("longitude") BigDecimal longitude,
                                                                               @Param("search") String search,
                                                                               Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq  AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC , (6371 * acos(cos(radians(:latitude)) * cos(radians(r.restaurantCoorX)) * cos(radians(r.restaurantCoorY) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(r.restaurantCoorX))))")
    Page<RestaurantListView> findALLByCategoryIdAndCoordinatesSortedByDistance(@Param("xMin") BigDecimal xMin,
                                                                               @Param("xMax") BigDecimal xMax,
                                                                               @Param("yMin") BigDecimal yMin,
                                                                               @Param("yMax") BigDecimal yMax,
                                                                               @Param("latitude") BigDecimal latitude,
                                                                               @Param("longitude") BigDecimal longitude,
                                                                               @Param("search") String search,
                                                                               @Param("userPk") Long userPk,
                                                                               Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.reviewAvgScore DESC")
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
                                                                         @Param("xMin") BigDecimal xMin,
                                                                         @Param("xMax") BigDecimal xMax,
                                                                         @Param("yMin") BigDecimal yMin,
                                                                         @Param("yMax") BigDecimal yMax,
                                                                         @Param("search") String search,
                                                                         Pageable pageable);


    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE (:categoryId = 0 OR r.restaurantPk IN (SELECT m.restaurant.seq FROM MatchingCategoryRestaurant m WHERE m.category.seq = :categoryId)) " +
            "AND r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.reviewAvgScore DESC")
    Page<RestaurantListView> findByCategoryIdAndCoordinatesSortedByScore(@Param("categoryId") Long categoryId,
                                                                         @Param("xMin") BigDecimal xMin,
                                                                         @Param("xMax") BigDecimal xMax,
                                                                         @Param("yMin") BigDecimal yMin,
                                                                         @Param("yMax") BigDecimal yMax,
                                                                         @Param("search") String search,
                                                                         @Param("userPk") Long userPk,
                                                                         Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.reviewAvgScore DESC, r.restaurantState ASC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinatesSortedByScore(@Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("search") String search,
                                                                            Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "AND (:search IS NULL OR r.restaurantName LIKE %:search% OR r.restaurantAddr LIKE %:search%)" +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.reviewAvgScore DESC")
    Page<RestaurantListView> findAllByCategoryIdAndCoordinatesSortedByScore(@Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("search") String search,
                                                                            @Param("userPk") Long userPk,
                                                                            Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN ResFollow rf ON r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE rf.resFollowPk is not NULL " +
            "AND r.restaurantState IN (1, 2) " +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC , rf.createdAt")
    Page<RestaurantListView> findFollowedRestaurant(@Param("userPk") Long userPk,
                                                    Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "where r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.createdAt desc")
    Page<RestaurantListView> findNewRestaurantListByCreatedAt(@Param("xMin") BigDecimal xMin,
                                                                         @Param("xMax") BigDecimal xMax,
                                                                         @Param("yMin") BigDecimal yMin,
                                                                         @Param("yMax") BigDecimal yMax,
                                                                         Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.createdAt desc")
    Page<RestaurantListView> findNewRestaurantListByCreatedAt(@Param("xMin") BigDecimal xMin,
                                                                            @Param("xMax") BigDecimal xMax,
                                                                            @Param("yMin") BigDecimal yMin,
                                                                            @Param("yMax") BigDecimal yMax,
                                                                            @Param("userPk") Long userPk,
                                                                            Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "0, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "where r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "and c.id is not null " +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.createdAt desc")
    Page<RestaurantListView> findCouponRestaurant(@Param("xMin") BigDecimal xMin,
                                                              @Param("xMax") BigDecimal xMax,
                                                              @Param("yMin") BigDecimal yMin,
                                                              @Param("yMax") BigDecimal yMax,
                                                              Pageable pageable);

    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "where r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "and c.id IS NOT NULL " +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.createdAt desc")
    Page<RestaurantListView> findCouponRestaurant(@Param("xMin") BigDecimal xMin,
                                                  @Param("xMax") BigDecimal xMax,
                                                  @Param("yMin") BigDecimal yMin,
                                                  @Param("yMax") BigDecimal yMax,
                                                  @Param("userPk") Long userPk,
                                                  Pageable pageable);



    @Query("SELECT new com.green.beadalyo.gyb.model.RestaurantListView( " +
            "r.restaurantPk, r.restaurantName, r.reviewAvgScore, r.reviewTotalElements, " +
            "r.restaurantAddr, r.restaurantState, r.restaurantPic, r.restaurantCoorX, " +
            "r.restaurantCoorY, r.createdAt, " +
            "CASE WHEN rf.resFollowPk IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END, " +
            "CASE WHEN c.id IS NOT NULL THEN c.price ELSE null END) " +
            "FROM RestaurantListView r " +
            "left join ResFollow rf on r.restaurantPk = rf.resPk.seq AND rf.userPk.userPk = :userPk " +
            "LEFT JOIN Coupon c ON c.restaurant.seq = r.restaurantPk " +
            "AND c.price = (SELECT MAX(c2.price) FROM Coupon c2 WHERE c2.restaurant.seq = r.restaurantPk) " +
            "LEFT JOIN Order o ON o.orderRes.seq = r.restaurantPk AND o.orderUser.userPk = :userPk " +
            "WHERE r.restaurantCoorX BETWEEN :xMin AND :xMax " +
            "AND r.restaurantCoorY BETWEEN :yMin AND :yMax " +
            "AND r.restaurantState IN (1, 2) " +
            "and c.id IS NOT NULL " +
            "group by r.restaurantPk " +
            "ORDER BY r.restaurantState ASC, r.createdAt desc")
    Page<RestaurantListView> findRecentOrderedList(@Param("xMin") BigDecimal xMin,
                                                  @Param("xMax") BigDecimal xMax,
                                                  @Param("yMin") BigDecimal yMin,
                                                  @Param("yMax") BigDecimal yMax,
                                                  @Param("userPk") Long userPk,
                                                  Pageable pageable);
}
