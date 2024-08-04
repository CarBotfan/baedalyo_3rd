package com.green.beadalyo.lmy.resfollow.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "res_follow"
        ,uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_pk", "res_pk"}
                )
        }
)
public class ResFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_follow_pk")
    private Long resFollowPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User userPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk")
    private Restaurant resPk;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
