package com.green.beadalyo.gyb.crollring;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "convertfood")
public class ConvertRestaurantBefore
{
    @Id
    Long id ;
    String name ;
    String address ;
    String logo_url ;
    String categories ;
    BigDecimal latitude ;
    BigDecimal longitude ;
}
