package com.green.beadalyo.gyb.response;

import com.green.beadalyo.gyb.model.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryRes
{
    private Long categoryPk ;
    private String categoryName ;
    private String categoryPic ;

    public CategoryRes(Category data)
    {
        this.categoryPk = data.getSeq() ;
        this.categoryName = data.getCategoryName() ;
        this.categoryPic = data.getCategoryPic() ;
    }

    public static List<CategoryRes> toCategoryRes(List<Category> categories)
    {
        List<CategoryRes> list = new ArrayList<>();
        for (Category category : categories)
        {
            CategoryRes categoryRes = new CategoryRes(category);
            list.add(categoryRes);
        }
        return list;
    }
}
