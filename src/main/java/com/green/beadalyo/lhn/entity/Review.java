package com.green.beadalyo.lhn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "done_order_pk")
    private Long doneOrderPk;

    @Column(name = "user_pk")
    private Long userPk;

    @Column(name = "res_pk")
    private Long resPk;

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