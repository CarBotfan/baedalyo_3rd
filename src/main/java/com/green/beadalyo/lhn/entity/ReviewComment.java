package com.green.beadalyo.lhn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "review_comment")
public class ReviewComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_pk")
    private Long reviewCommentPk;

    @Column(name = "review_pk")
    private Long reviewPk;

    @Column(name = "comment_content")
    private String commentContent;
}