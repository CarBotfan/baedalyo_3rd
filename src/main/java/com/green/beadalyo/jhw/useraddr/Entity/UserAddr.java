package com.green.beadalyo.jhw.useraddr.Entity;

import com.green.beadalyo.jhw.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserAddr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addrPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr_user_pk", nullable = false)
    private User user;

    @Column(nullable = false)
    private String addrName;

    @Column(length = 50, nullable = false)
    private String addr1;

    @Column(length = 50, nullable = false)
    private String addr2;

    private Long addr_default;

    @Column(nullable = false)
    private BigDecimal addrCoorX;

    @Column(nullable = false)
    private BigDecimal addrCoorY;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
