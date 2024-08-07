package com.green.beadalyo.kdh.admin.entity;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lhn.Review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @Column(name = "report_title",nullable = false)
    private String reportTitle;

    @Column(name = "report_content",nullable = false)
    private String reportContent;

    @ColumnDefault("1")
    @Column(name = "report_state")
    private int reportState;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
