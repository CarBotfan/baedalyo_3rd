package com.green.beadalyo.kdh.admin.entity;

import com.green.beadalyo.jhw.user.entity.User;
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
@Table(name = "inquiry")
public class InquiryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_pk")
    private Long inquiryPk;

    @ManyToOne
    @JoinColumn(name = "user_pk", nullable = false)
    private User userPk;

    @Column(name = "inquiry_title")
    private String inquiryTitle;

    @Column(name = "inquiry_content")
    private String inquiryContent;

    @ColumnDefault("1")
    @Column(name = "inquiry_state", nullable = false)
    private int inquiryState;

    @Column(name = "inquiry_response")
    private String inquiryResponse;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
