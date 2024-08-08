package com.green.beadalyo.kdh.accept.model;

import com.green.beadalyo.gyb.model.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetUnAcceptRestaurant {
    private Long resPk;
    private Long resUserPk;
    private String resName;
    private String resRegiNum;
    private String resAddr;
    private String resDescription1;
    private String resDescription2;
    private LocalDateTime createdAt;

    public GetUnAcceptRestaurant(Restaurant data) {
        this.resPk = data.getSeq();
        this.resUserPk = data.getUser().getUserPk();
        this.resName = data.getName();
        this.resRegiNum = data.getRegiNum();
        this.resAddr = data.getAddress();
        this.resDescription1 = data.getRestaurantDescription();
        this.resDescription2 = data.getReviewDescription();
        this.createdAt = data.getInputDt();
    }

}
