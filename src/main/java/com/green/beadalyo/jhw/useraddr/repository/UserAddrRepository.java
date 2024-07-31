package com.green.beadalyo.jhw.useraddr.repository;

import com.green.beadalyo.jhw.useraddr.Entity.UserAddr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAddrRepository extends JpaRepository<UserAddr, Long> {
    @Query("select ua from UserAddr ua WHERE ua.addrDefault = :userPk AND ua.user.userPk = :userPk")
    UserAddr findMainUserAddr(Long userPk);

    @Modifying
    @Query("update UserAddr ua SET ua.addrDefault = :userPk" +
            " WHERE ua.user.userPk = :userPk AND ua.addrPk = :addrPk")
    int setMainUserAddr(Long addrPk, Long userPk);

    @Modifying
    @Query("update UserAddr ua SET ua.addrDefault = null " +
            "WHERE ua.user.userPk = :userPk AND ua.addrDefault = :userPk")
    void removeMainUserAddr(Long userPk);

    @Query("select ua from UserAddr ua WHERE ua.user.userPk = :userPk order by ua.addrDefault desc, ua.createdAt desc")
    List<UserAddr> findAllByUserPkOrderByAddrDefaultDesc(Long userPk);

    @Query("select ua from UserAddr ua WHERE ua.user.userPk = :userPk AND ua.addrPk = :addrPk")
    UserAddr findUserAddrByUserPkAnAndAddrPk(Long addrPk, Long userPk);
}
