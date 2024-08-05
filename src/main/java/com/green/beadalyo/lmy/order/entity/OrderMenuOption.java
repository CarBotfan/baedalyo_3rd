package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.kdh.menuOption.entity.MenuOption;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_menu_option")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderMenuOption
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_option_pk")
    private Long seq ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_menu_pk")
    private OrderMenu orderMenu ;

    @Column(name = "option_name", length = 20,nullable = false)
    private String optionName;

    @Column(name = "option_price",nullable = false)
    private Integer optionPrice;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt ;

    public OrderMenuOption(MenuOption data, OrderMenu orderMenu)
    {
        this.orderMenu = orderMenu ;
        this.optionName = data.getOptionName() ;
        this.optionPrice = data.getOptionPrice() ;
        this.createdAt = LocalDateTime.now() ;
    }

    public static List<OrderMenuOption> toOrderMenuOptionList(List<MenuOption> data, OrderMenu orderMenu)
    {
        List<OrderMenuOption> list = new ArrayList<>() ;
        for (MenuOption i : data)
        {
            OrderMenuOption menuOption = new OrderMenuOption(i,orderMenu) ;
            list.add(menuOption) ;
        }

        return list ;

    }

}
