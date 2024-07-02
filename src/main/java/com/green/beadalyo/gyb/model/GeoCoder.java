package com.green.beadalyo.gyb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
import java.math.BigDecimal;

@Entity
@Table(name = "food")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoCoder
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq ;

    private String name ;
    private String category ;
    private String address ;

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude ;
    @Column(precision = 9, scale = 6)
    private BigDecimal longitude ;
}
