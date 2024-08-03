package com.green.beadalyo.jhw.MenuCategory;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.MenuCategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCatPatchReq;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCatPositionPatchReq;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategory;
import com.green.beadalyo.jhw.MenuCategory.model.MenuCategoryInsertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class MenuCategoryService {
    private final MenuCategoryRepository repository;

    @Transactional
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

    public MenuCategory getMenuCat(Long menuCatPk) {
        if(!repository.existsById(menuCatPk)) {
            throw new MenuCatNotFoundException();
        }
        return repository.getReferenceById(menuCatPk);
    }

    @Transactional
    public int deleteMenuCat(Long menuCatPk) {
        if(!repository.existsById(menuCatPk)) {
            throw new MenuCatNotFoundException();
        }
        MenuCategory menuCat = repository.getReferenceById(menuCatPk);
        List<MenuCategory> list = repository.findMenuCategoriesByRestaurantOrderByPosition(menuCat.getRestaurant());
        for(MenuCategory mc : list) {
            if(mc.getPosition() > menuCat.getPosition()) {
                mc.setPosition(mc.getPosition() - 1);
            }
        }
        repository.delete(repository.getReferenceById(menuCatPk));
        return 1;
    }
    @Transactional
    public int patchMenuCat(MenuCatPatchReq p) {
        if(!repository.existsById(p.getMenuCatPk())) {
            throw new MenuCatNotFoundException();
        }
        MenuCategory menuCategory = repository.getReferenceById(p.getMenuCatPk());
        menuCategory.setMenuCatName(p.getMenuCatName());
        return 1;
    }

    @Transactional
    public int patchMenuCatPosition(MenuCatPositionPatchReq p) {
        if(!repository.existsById(p.getMenuCatPk1()) || !repository.existsById(p.getMenuCatPk2())) {
            throw new MenuCatNotFoundException();
        }
        MenuCategory menuCategory1 = repository.getReferenceById(p.getMenuCatPk1());
        MenuCategory menuCategory2 = repository.getReferenceById(p.getMenuCatPk2());
        Long position1 = menuCategory1.getPosition();

        menuCategory1.setPosition(menuCategory2.getPosition());
        menuCategory2.setPosition(position1);
        return 1;
    }

}
