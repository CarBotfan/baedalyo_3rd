package com.green.beadalyo.kdh.report.entity;

import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.lhn.Review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "report",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"review_pk", "user_pk"})
        }
)
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_pk")
    private Long reportPk;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "review_pk", nullable = false)
    private Review reviewPk;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_pk",nullable = false)
    private User user;

    @Column(name = "report_title",nullable = false)
    private String reportTitle;

    @Column(name = "report_content",nullable = false)
    private String reportContent;

    @ColumnDefault("1")
    @Column(name = "report_state",nullable = false)
    private int reportState;

    @Column(name = "report_result")
    private String reportResult;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
