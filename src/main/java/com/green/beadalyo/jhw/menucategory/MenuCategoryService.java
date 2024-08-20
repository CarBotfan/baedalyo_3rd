package com.green.beadalyo.jhw.menucategory;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.menucategory.exception.MenuCatNotFoundException;
import com.green.beadalyo.jhw.menucategory.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class MenuCategoryService {
    private final MenuCategoryRepository repository;

    @Transactional
    public void insertMenuCat(MenuCatInsertDto dto) {
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
    public int deleteMenuCat(Long menuCatPk, Restaurant restaurant) {
        if(!repository.existsByMenuCategoryPkAndRestaurant(menuCatPk, restaurant)) {
            throw new MenuCatNotFoundException();
        }
        MenuCategory menuCat = repository.findByMenuCategoryPkAndRestaurant(menuCatPk, restaurant);
        List<MenuCategory> list = repository.findMenuCategoriesByRestaurantOrderByPosition(restaurant);
        for(MenuCategory mc : list) {
            if(mc.getPosition() > menuCat.getPosition()) {
                mc.setPosition(mc.getPosition() - 1);
            }
        }

        repository.delete(menuCat);
        return 1;
    }
    @Transactional
    public int patchMenuCat(MenuCatPatchDto dto) {
        if(!repository.existsByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), dto.getRestaurant())) {
            throw new MenuCatNotFoundException();
        }
        MenuCategory menuCategory = repository.findByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), dto.getRestaurant());
        menuCategory.setMenuCatName(dto.getMenuCatName());
        return 1;
    }

    @Transactional
    public int patchMenuCatPosition(MenuCatPositionPatchDto dto) {
        if(!repository.existsByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), dto.getRestaurant())) {
            throw new MenuCatNotFoundException();
        }
        if(dto.getPosition() == 0L) {
            throw new RuntimeException("올바르지 않은 위치입니다");
        }
        MenuCategory menuCategory = repository.getReferenceById(dto.getMenuCatPk());
        if(menuCategory.getPosition() > dto.getPosition()) {
            List<MenuCategory> list = repository.findMenuCategoriesByPositionBetweenAndRestaurant(dto.getRestaurant(), dto.getPosition(), menuCategory.getPosition());
            for(MenuCategory mc : list) {
                if(mc.getPosition() >= dto.getPosition()) {
                    mc.setPosition(mc.getPosition() + 1);
                }
            }
        } else if(menuCategory.getPosition() < dto.getPosition()) {
            List<MenuCategory> list = repository.findMenuCategoriesByPositionBetweenAndRestaurant(dto.getRestaurant(), menuCategory.getPosition(), dto.getPosition());
            if((list.size() <= 1 && dto.getPosition() > 1) || list.get(list.size()-1).getPosition() < dto.getPosition()) {
                throw new RuntimeException("올바르지 않은 위치입니다");
            }
            for(MenuCategory mc : list) {
                if(mc.getPosition() <= dto.getPosition()) {
                    mc.setPosition(mc.getPosition() - 1);
                }
            }
        }

        menuCategory.setPosition(dto.getPosition());
        return 1;
    }



}
