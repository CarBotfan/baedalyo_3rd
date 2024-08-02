package com.green.beadalyo.jhw.MenuCategory;

import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.MenuCategory.MenuCategoryRepository;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategory;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategoryInsertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    public List<MenuCategory> getMenuCatList(Restaurant restaurant) {
        return repository.findMenuCategoriesByRestaurantOrderByPosition(restaurant);
    }

    public MenuCategory getMenuCat(Restaurant restaurant, Long position) {
        if(!repository.existsByRestaurantAndPosition(restaurant, position)) {
            throw new RuntimeException("정보 조회 실패");
        }
        return repository.findMenuCategoryByRestaurantAndPosition(restaurant, position);
    }

    @Transactional
    public int deleteMenuCat(Restaurant res, Long position) {
        if(!repository.existsByRestaurantAndPosition(res, position)) {
            throw new RuntimeException("정보 조회 실패");
        }
        repository.delete(repository.findMenuCategoryByRestaurantAndPosition(res, position));
        List<MenuCategory> list = repository.findMenuCategoriesByRestaurantOrderByPosition(res);
        for(MenuCategory mc : list) {
            if(mc.getPosition() > position) {
                mc.setPosition(mc.getPosition() - 1);
            }
        }
        return 1;
    }
}
