package com.green.beadalyo.jhw.user.repository;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.model.UserInfoGetRes;
import com.green.beadalyo.kdh.stat.admin.model.GetDailySignOutCount;
import com.green.beadalyo.kdh.stat.admin.model.GetDailySignUpCount;
import com.green.beadalyo.kdh.stat.admin.model.GetMonthSignOutCount;
import com.green.beadalyo.kdh.stat.admin.model.GetMonthSignUpCount;
import com.green.beadalyo.lmy.doneorder.model.DailyOrderCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(String userId);

    Boolean existsByUserEmail(String userEmail);
    Boolean existsByUserNickname(String userNickname);
    Boolean existsByUserPhone(String userPhone);
    Boolean existsByUserId(String userId);
    User findByUserEmail(String userEmail);
    User findByUserPk(Long userPk);
    User findByUserEmailAndUserName(String userEmail, String userName);
    User findByUserEmailAndUserNameAndUserId(String userEmail, String userName, String userId);


    //어드민 통계부분에서 쓸 부분입니당

    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m-%d') AS createdAt, " +
                         "COUNT(u.user_pk) AS dailySignUpCount " +
                    "FROM user u " +
                    "WHERE DATE_FORMAT(u.created_at, '%Y-%m') = :date " +
                    "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m-%d')", nativeQuery = true)
    List<GetDailySignUpCount> getDailySignUpCount(@Param("date") String date);

    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m') AS createdAt, " +
            "COUNT(u.user_pk) AS monthSignUpCount " +
            "FROM user u " +
            "WHERE DATE_FORMAT(u.created_at, '%Y') = :date " +
            "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m')", nativeQuery = true)
    List<GetMonthSignUpCount> getMonthSignUpCount(@Param("date") String date);

    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m-%d') AS createdAt, " +
            "COUNT(u.user_pk) AS dailySignOutCount " +
            "FROM user u " +
            "WHERE DATE_FORMAT(u.created_at, '%Y-%m') = :date " +
            "and u.user_state = 3 "+
            "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m-%d')", nativeQuery = true)
    List<GetDailySignOutCount> getDailySignOutCount(@Param("date") String date);

    @Query(value = "SELECT DATE_FORMAT(u.created_at, '%Y-%m') AS createdAt, " +
            "COUNT(u.user_pk) AS monthSignOutCount " +
            "FROM user u " +
            "WHERE DATE_FORMAT(u.created_at, '%Y') = :date " +
            "and u.user_state = 3 "+
            "GROUP BY DATE_FORMAT(u.created_at, '%Y-%m')", nativeQuery = true)
    List<GetMonthSignOutCount> getMonthSignOutCount(@Param("date") String date);
}
