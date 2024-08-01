package com.green.beadalyo.lhn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_pk")
    private Long resPk;

    @Column(name = "res_user_pk")
    private Long resUserPk;

    @Column(name = "res_name")
    private String resName;
}