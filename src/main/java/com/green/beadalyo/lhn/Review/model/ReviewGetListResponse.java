package com.green.beadalyo.lhn.Review.model;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ReviewGetListResponse {
    private Integer page;
    private Integer totalPage;
    private List<ReviewGetRes> reviewList;
}
