package com.green.beadalyo.lhn.Review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "review_comment")
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_pk")
    private Long reviewCommentPk;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_pk",nullable = false)
    private Review reviewPk;

    @Column(name = "comment_content")
    private String commentContent;
}