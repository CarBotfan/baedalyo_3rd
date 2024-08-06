package com.green.beadalyo.lmy.doneorder.entity;

import com.green.beadalyo.lmy.order.entity.OrderMenuOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "done_order_menu_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoneOrderMenuOption
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "done_order_menu_option_pk")
    private Long seq ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "done_order_menu_pk",nullable = false)
    private DoneOrderMenu doneOrderMenu;

    @Column(name = "option_name", length = 20,nullable = false)
    private String optionName;

    @Column(name = "option_price",nullable = false)
    private Integer optionPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt ;


    public DoneOrderMenuOption(OrderMenuOption data)
    {
        this.doneOrderMenu = null ;
        this.optionName = data.getOptionName() ;
        this.optionPrice = data.getOptionPrice() ;
    }


}
