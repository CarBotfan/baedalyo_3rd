package com.green.beadalyo.kdh.admin.entity;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lhn.Review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "report")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_pk")
    private Long reportPk;

    @ManyToOne
    @JoinColumn(name = "review_pk", nullable = false)
    private Review reviewPk;

    @ManyToOne
    @JoinColumn(name = "user_pk",nullable = false)
    private User userPk;

    @Column(name = "report_content")
    private String reportContent;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
