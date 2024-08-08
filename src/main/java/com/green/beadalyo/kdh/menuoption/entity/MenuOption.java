package com.green.beadalyo.kdh.menuoption.entity;

import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "option")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOption
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_pk")
    private Long seq ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "option_menu_pk")
    private MenuEntity menu ;

    @Column(name = "option_name", nullable = false , length = 20)
    @Comment("옵션 이름")
    private String optionName ;

    @Column(name = "option_price", nullable = false)
    @Comment("옵션 추가 가격")
    private Integer optionPrice ;

    @Column(name = "option_state", nullable = false)
    @ColumnDefault("1")
    @Comment("옵션 상태 1 : 정상 / 2 : 품절")
    private Integer optionState ;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt ;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt ;

}
