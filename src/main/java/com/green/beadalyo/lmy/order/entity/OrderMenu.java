package com.green.beadalyo.lmy.order.entity;

import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.lmy.order.model.OrderMenuReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Data
@Table(name = "order_menu")
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_pk")
    private Long orderMenuPk;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_pk", nullable = false)
    private Order order;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_pk", nullable = false)
    private MenuEntity menuPk;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "orderMenu")
    private List<OrderMenuOption> orderMenuOption;

    @Column(name = "menu_name", length = 20, nullable = false)
    private String menuName;

    @Column(name = "menu_price", nullable = false)
    private Integer menuPrice;

    @Column(name = "menu_count", nullable = false)
    private Integer menuCount ;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public OrderMenu(MenuEntity data, Order order, Integer count, List<Long> optionPk)
    {
        this.order = order ;
        this.menuPk = data ;
        this.menuName = data.getMenuName() ;
        this.menuPrice = data.getMenuPrice() ;
        this.menuCount = count ;
        this.orderMenuOption = new ArrayList<>();
        optionPk.forEach(menuOption -> {
            data.getOptionList().stream()
                    .filter(optionReq -> menuOption.equals(optionReq.getSeq()))
                    .forEach(option -> {
                        OrderMenuOption orderMenuOption = new OrderMenuOption();
                        orderMenuOption.setOrderMenu(this);
                        orderMenuOption.setOptionName(option.getOptionName());
                        orderMenuOption.setOptionPrice(option.getOptionPrice());
                        this.orderMenuOption.add(orderMenuOption);
                    });
        });

    }

    public static List<OrderMenu> toOrderMenuList(List<MenuEntity> list, Order order, List<OrderMenuReq> menu)
    {
        List<OrderMenu> orderMenuList = new ArrayList<>();

        list.forEach(menuEntity -> {
            for (OrderMenuReq orderMenuReq : menu) {
                if (orderMenuReq.getMenuPk().equals(menuEntity.getMenuPk())) {
                    OrderMenu data = new OrderMenu(menuEntity, order, orderMenuReq.getMenuCount(), orderMenuReq.getMenuOptionPk()) ;
                    orderMenuList.add(data);
                    menu.remove(orderMenuReq) ;
                    break;
                }
            }

        });

        return orderMenuList;
    }


}
