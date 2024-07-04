package com.green.beadalyo.gyb.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cate_res_matching")
public class MatchingCategoryRestaurant
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crm_pk")
    private Long seq ;

    @ManyToOne
    @JoinColumn(name = "crm_res_pk")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "crm_cate_pk")
    private Category category;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt ;

    public MatchingCategoryRestaurant(Restaurant restaurant, Category category)
    {
        this.restaurant = restaurant;
        this.category = category;
    }

}
