package com.green.beadalyo.jhw.useraddr.repository;

import com.green.beadalyo.jhw.user.entity.UserEntity;
import com.green.beadalyo.jhw.user.model.User;
import com.green.beadalyo.jhw.user.model.UserSignUpPostReq;
import com.green.beadalyo.jhw.useraddr.Entity.UserAddr;
import com.green.beadalyo.jhw.useraddr.model.MainUserAddrPatchReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAddrRepository extends JpaRepository<UserAddr, Long> {
    @Query("select ua from UserAddr ua WHERE ua.addrDefault = :userPk AND ua.userEntity.userPk = :userPk")
    UserAddr findMainUserAddr(Long userPk);

    @Modifying
    @Query("update UserAddr ua SET ua.addrDefault = :userPk" +
            " WHERE ua.userEntity.userPk = :userPk AND ua.addrPk = :addrPk")
    int setMainUserAddr(Long addrPk, Long userPk);

    @Modifying
    @Query("update UserAddr ua SET ua.addrDefault = null " +
            "WHERE ua.userEntity.userPk = :userPk AND ua.addrDefault = :userPk")
    void removeMainUserAddr(Long userPk);

    @Query("select ua from UserAddr ua WHERE ua.userEntity.userPk = :userPk")
    List<UserAddr> findAllByUserPk(Long userPk);

    @Query("select ua from UserAddr ua WHERE ua.userEntity.userPk = :userPk AND ua.addrPk = :addrPk")
    UserAddr findUserAddrByUserPkAnAndAddrPk(Long addrPk, Long userPk);
}
