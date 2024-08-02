package com.green.beadalyo.jhw.MenuCategory;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.MenuCategory.MenuCategoryRepository;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategory;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategoryInsertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuCategoryService {
    private final MenuCategoryRepository repository;

    public void insertMenuCat(MenuCategoryInsertDto dto) {
        if(dto.getMenuCatName().length() > 50) {
            throw new RuntimeException("카테고리명의 길이는 최대 50자입니다.");
        }
        MenuCategory menuCat = new MenuCategory(dto);
        repository.save(menuCat);
    }

    public Long getMenuCatCount(Restaurant restaurant) {
        return repository.countMenuCategories(restaurant);
    }
}
