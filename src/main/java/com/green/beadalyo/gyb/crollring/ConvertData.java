package com.green.beadalyo.gyb.crollring;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ConvertData
{
    private List<Item> items ;
    private String slug ;
    private String name ;
    private String description ;
    private String image ;
    private Long id ;
}


@Data
class Item
{
    private String original_image;
    private Boolean soldout ;
    private Long menu_set_id ;
    private Integer price ;
    private String slug;
    private String description;
    private String name ;
    private List<SubchoiceSection> subchoices;
}


@Data
class SubchoiceSection {
    private boolean multiple;
    private String name;
    private Integer multiple_count;
    private boolean has_deposit;
    private boolean is_available_quantity;
    private String slug;
    private List<Subchoice> subchoices;
    private boolean mandatory;
    private int id;

}

@Data
class Subchoice {
    private String slug;
    private String name;
    private String price;
    private boolean soldout;
    private int deposit_price;
    private String deposit_description;
    private int id;
    private boolean is_deposit;
    private String description;
}


