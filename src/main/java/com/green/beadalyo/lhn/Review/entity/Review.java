package com.green.beadalyo.lhn.Review.entity;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lmy.doneorder.entity.DoneOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_pk")
    private Long reviewPk;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "done_order_pk", nullable = false)
    private DoneOrder doneOrderPk;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk", nullable = false)
    private User userPk;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_pk", nullable = false)
    private Restaurant resPk;

    @Column(name = "review_contents")
    private String reviewContents;

    @Column(name = "review_rating")
    private int reviewRating;

    @Column(name = "review_pics_1")
    private String reviewPics1;

    @Column(name = "review_pics_2")
    private String reviewPics2;

    @Column(name = "review_pics_3")
    private String reviewPics3;

    @Column(name = "review_pics_4")
    private String reviewPics4;

    @Column(name = "review_state")
    private int reviewState;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}