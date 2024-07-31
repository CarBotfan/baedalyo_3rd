package com.green.beadalyo.jhw.useraddr.Entity;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.useraddr.model.UserAddrPostReq;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "address")
public class UserAddr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addr_pk")
    private long addrPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addr_user_pk", nullable = false)
    private User user;

    @Column(name = "addr_name" ,nullable = false)
    private String addrName;

    @Column(name = "addr1", length = 50, nullable = false)
    private String addr1;

    @Column(name = "addr2", length = 50, nullable = false)
    private String addr2;

    @Column(name = "addr_default", unique = true)
    private Long addrDefault;

    @Column(name = "addr_coor_x", nullable = false)
    private BigDecimal addrCoorX;

    @Column(name = "addr_coor_y", nullable = false)
    private BigDecimal addrCoorY;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserAddr(UserAddrPostReq p) {
        this.addrName = p.getAddrName();
        this.addr1 = p.getAddr1();
        this.addr2 = p.getAddr2();
        this.addrCoorX = p.getAddrCoorX();
        this.addrCoorY = p.getAddrCoorY();
    }

}
