package com.green.beadalyo.gyb.model;

import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.request.RestaurantManagePatchReq;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_pk")
    @Comment("고유 번호")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("소유자 정보")
    @JoinColumn(name = "res_user_pk")
    private User user;

    @Column(name = "res_name", nullable = false, length = 50)
    @Comment("음식점 이름")
    private String name;

    @Column(name = "res_description1", length = 300)
    @Comment("가게 설명")
    private String RestaurantDescription;

    @Column(name = "res_description2", length = 300)
    @Comment("가게 설명")
    private String reviewDescription;

    @Column(name = "res_addr" , nullable = false , length = 100)
    @Comment("음식점 주소")
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cate_res_matching",
            joinColumns = @JoinColumn(name = "crm_res_pk"),
            inverseJoinColumns = @JoinColumn(name = "crm_cate_pk")
    )
    List<Category> categories;



    @Column(name = "res_regi_num", nullable = false , length = 12)
    @Comment("사업자 등록번호")
    private String regiNum;

    @Column(name = "res_coor_x")
    @Comment("음식점 위도(X좌표)")
    private Double coorX;

    @Column(name = "res_coor_y")
    @Comment("음식점 경도(y좌표)")
    private Double coorY;

    @Column(name = "res_open")
    @Comment("오픈 시간")
    private LocalTime openTime;

    @Column(name = "res_close")
    @Comment("종료 시간")
    private LocalTime closeTime;

    @Column(name = "res_state")
    @Comment("현재 영업 상태 1 : 영업 중 / 2 : 휴업 / 3 : 폐점, 디폴트 2")
    @ColumnDefault("2")
    private Integer state;

    @Column(name = "res_pic", length = 100)
    @Comment("음식점 사진")
    private String pic ;

    @Column(name = "created_at")
    @Comment("생성일")
    @CreationTimestamp
    private LocalDateTime inputDt;

    @Column(name = "updated_at")
    @Comment("수정일")
    @UpdateTimestamp
    private LocalDateTime updateDt ;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menuResPk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuEntity> menuList = new ArrayList<>(); ;

    public Restaurant(RestaurantInsertDto data)
    {
        this.user = data.getUser() ;
        this.name = data.getName() ;
        this.address = data.getResAddr() ;
        this.regiNum = data.getRegiNum() ;
        this.coorX = data.getResCoorX() ;
        this.coorY = data.getResCoorY() ;
        this.openTime = data.getOpenTime() ;
        this.closeTime = data.getCloseTime() ;
        this.RestaurantDescription = data.getDesc1() ;
        this.reviewDescription = data.getDesc2() ;
        this.pic = data.getResPic() ;
        this.state = 2 ;
    }

    public void update(RestaurantManagePatchReq data)
    {
        if (data.getRestaurantName() != null && !data.getRestaurantName().isEmpty())
            this.name = data.getRestaurantName() ;
        if (data.getAddr() != null && !data.getAddr().isEmpty())
            this.address = data.getAddr() ;
        if (data.getRegiNum() != null && !data.getRegiNum().isEmpty())
            this.regiNum = data.getRegiNum() ;
        if (data.getCoorX() != null)
            this.coorX = data.getCoorX() ;
        if (data.getCoorY() != null)
            this.coorY = data.getCoorY() ;
        if (data.getRestaurantDescription() != null && !data.getRestaurantDescription().isEmpty())
            this.RestaurantDescription = data.getRestaurantDescription() ;
        if (data.getReviewDescription() != null && !data.getReviewDescription().isEmpty())
            this.reviewDescription = data.getReviewDescription() ;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = data.getOpenTime() ;
        if (time != null)
            this.openTime = LocalTime.parse(time,formatter) ;
        time = data.getCloseTime() ;
        if (time != null)
            this.closeTime = LocalTime.parse(time,formatter) ;

    }

}
