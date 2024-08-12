package com.green.beadalyo.kdh.menuoption;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.repository.MenuRepository;
import com.green.beadalyo.kdh.menuoption.entity.MenuOption;
import com.green.beadalyo.kdh.menuoption.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final MenuOptionRepository menuOptionRepository ;
    private final MenuRepository menuRepository ;

    public List<MenuOption> getListIn(List<Long> ids)
    {
        return menuOptionRepository.findBySeqIn(ids) ;
    }

//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public MenuOption makeOptionEntity(PostMenuOptionReq req) {
        MenuOption entity = new MenuOption();
        MenuEntity menuEntity = menuRepository.getReferenceById(req.getOptionMenuPk());
        entity.setMenu(menuEntity);
        entity.setOptionName(req.getOptionName());
        entity.setOptionPrice(req.getOptionPrice());
        entity.setOptionState(1);

        return entity;
    }

    public MenuOption saveOption(MenuOption entity) {
        return menuOptionRepository.save(entity);
    }

    public boolean validateMenuOwner(Long menuPk) {
        MenuEntity menuEntity = menuRepository.getReferenceById(menuPk);
        Restaurant restaurant = menuEntity.getMenuResPk();
        User resUser = restaurant.getUser();
        long resUserPk = resUser.getUserPk();
        long userPk = authenticationFacade.getLoginUserPk();

        return resUserPk == userPk;
    }

    public PostMenuOptionRes makePostMenuOptionRes(MenuOption entity) {
        PostMenuOptionRes res = new PostMenuOptionRes();
        res.setOptionPk(entity.getSeq());
        res.setOptionName(entity.getOptionName());
        res.setOptionPrice(entity.getOptionPrice());
        res.setOptionMenuPk(entity.getMenu().getMenuPk());
        res.setOptionPrice(entity.getOptionPrice());
        res.setOptionState(entity.getOptionState());
        return res;
    }

    public PutMenuOptionRes makePutMenuOptionRes(MenuOption entity) {
        PutMenuOptionRes res = new PutMenuOptionRes();
        res.setOptionName(entity.getOptionName());
        res.setOptionPrice(entity.getOptionPrice());
        res.setOptionMenuPk(entity.getMenu().getMenuPk());
        res.setOptionPrice(entity.getOptionPrice());
        res.setOptionState(entity.getOptionState());
        return res;
    }

    public MenuOption updateOptionEntity(PutMenuOptionReq req) {
        MenuOption entity = menuOptionRepository.getReferenceById(req.getOptionPk());
        entity.setOptionName(req.getOptionName());
        entity.setOptionPrice(req.getOptionPrice());
        return entity;
    }

    public MenuOption changeOptionState(Long optionPk) {
        MenuOption entity = menuOptionRepository.getReferenceById(optionPk);
        Integer state = entity.getOptionState();

        switch (state) {
            case 1: entity.setOptionState(2);
            case 2: entity.setOptionState(1);
            default: ;
        }

        return entity;
    }

    public MenuOption deleteOption(Long optionPk) {
        MenuOption entity = menuOptionRepository.getReferenceById(optionPk);
        entity.setOptionState(3);

        return entity;
    }

    public List<GetMenuOptionRes> getMenuOptions(Long menuPk) {
        return menuOptionRepository.findMenuOptionResByMenu(menuPk);
    }
}
