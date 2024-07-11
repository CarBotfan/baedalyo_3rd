package com.green.beadalyo.gyb.common;

import com.green.beadalyo.gyb.response.RestaurantListRes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPage<T>
{
    private List<T> list;
    private Integer totalElements ;
    private Integer totalPages ;

    public ResultPage(Page<T> page)
    {
        this.list = page.toList();
        this.totalElements = (int) page.getTotalElements() ;
        this.totalPages = page.getTotalPages() ;
    }
}
