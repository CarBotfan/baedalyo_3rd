package com.green.beadalyo.gyb.model;

import com.green.beadalyo.gyb.response.CategoryRes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_pk")
    private Long seq ;

    @Column(name = "cate_name" , nullable = false , length = 20)
    private String categoryName ;

    @Column(name = "cate_pic" , length = 100)
    private String categoryPic ;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<Restaurant> restaurants = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createAt ;


    public Category(String categoryName, String categoryPic)
    {
        this.categoryName = categoryName;
        this.categoryPic = categoryPic;
    }


}
