package com.green.beadalyo.kdh.menu;


import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository2;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.model.*;
import com.green.beadalyo.kdh.menu.repository.MenuRepository;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionReq;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final MenuMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final AuthenticationFacade authenticationFacade;
    private final long maxSize = 3145728;
    private final MenuRepository menuRepository;
    private final UserRepository2 userRepository;
    private final RestaurantRepository restaurantRepository;
    @Transactional
    public long postMenu(PostMenuReq p,
                                MultipartFile pic){

        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(p.getResUserPk()));

        if (restaurant == null) {
            throw new RuntimeException();
        }
        p.setMenuResPk(restaurant.getSeq());

        List<String> menuName = mapper.getMenuName(p.getMenuResPk());
        for (String menu : menuName){
            if (menu.equals(p.getMenuName())){
                throw new IllegalArgumentException();
            }
        }

        MenuEntity menuEntity = new MenuEntity();
        if (pic==null || pic.isEmpty()){
            menuEntity.setMenuPic(null);
            menuEntity.setMenuResPk(restaurant);
            menuEntity.setMenuContent(p.getMenuContent());
            menuEntity.setMenuName(p.getMenuName());
            menuEntity.setMenuPrice(p.getMenuPrice());
            menuEntity.setMenuState(p.getMenuState());
            MenuEntity menuEntity1 =  menuRepository.save(menuEntity);

            return menuEntity1.getMenuPk();
        }
        if(pic.getSize() > maxSize){
            throw new ArithmeticException();
        }
        String picName = customFileUtils.makeRandomFileName(pic);
        menuEntity.setMenuPic(picName);
        menuEntity.setMenuResPk(restaurant);
        menuEntity.setMenuContent(p.getMenuContent());
        menuEntity.setMenuName(p.getMenuName());
        menuEntity.setMenuPrice(p.getMenuPrice());
        menuEntity.setMenuState(p.getMenuState());
        MenuEntity menuEntity1 =  menuRepository.save(menuEntity);
        String path = String.format("menu/%d", menuEntity1.getMenuPk());
        p.setMenuPic(path + "/"+ picName);
        menuRepository.updateMenuPic(p.getMenuPic(), menuEntity1.getMenuPk());
        menuEntity1.setMenuPic(p.getMenuPic());

        try {
            customFileUtils.makeFolder(path);
            String target = String.format("%s/%s",path,picName);
            customFileUtils.transferTo(pic,target);
        } catch (Exception e){
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
        }

        return menuEntity1.getMenuPk();
    }

    public List<GetAllMenuRes> getAllMenu(GetAllMenuReq p){
        return mapper.getAllMenu(p);
    }

    public List<GetAllMenuResInterface> getAllMenuByUserPk(){
        long userPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(userPk));
        if (restaurant == null || restaurant.getSeq() == 0 ){
            throw new NullPointerException();
        }

        return menuRepository.findAllByMenuResPk(restaurant.getSeq());
    }

    @Transactional
    public long putMenu(MultipartFile pic, PutMenuReq p){

            long userPk = authenticationFacade.getLoginUserPk();
            Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(userPk));
            MenuEntity menuEntity = menuRepository.getReferenceById(p.getMenuPk());

            if (restaurant.getSeq() != menuEntity.getMenuResPk().getSeq()){
                throw new RuntimeException();
            }

            long menuResPk = mapper.getMenuResPkByMenuPk(p.getMenuPk());
            List<String> menuName = mapper.getMenuNameByPut(menuResPk,p.getMenuPk());
            for (String menu : menuName){
                if (menu.equals(p.getMenuName())){
                    throw new IllegalArgumentException();
                }
            }

        if (pic == null || pic.isEmpty() ){
            menuEntity.setMenuContent(p.getMenuContent());
            menuEntity.setMenuName(p.getMenuName());
            menuEntity.setMenuPrice(p.getMenuPrice());
            menuEntity.setMenuState(p.getMenuState());
            menuEntity.setMenuPic(null);
            MenuEntity result = menuRepository.save(menuEntity);

            return result.getMenuPk();
        }
        if(pic.getSize() > maxSize){
            throw new ArithmeticException();
        }
        MenuEntity result = null;
        try {
            String path = String.format("menu/%d",p.getMenuPk());
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
            customFileUtils.makeFolder(path);
            String picName = customFileUtils.makeRandomFileName(pic);
            String target = String.format("%s/%s",path,picName);
            customFileUtils.transferTo(pic,target);
            p.setMenuPic(path + "/" + picName);
            menuEntity.setMenuContent(p.getMenuContent());
            menuEntity.setMenuName(p.getMenuName());
            menuEntity.setMenuPrice(p.getMenuPrice());
            menuEntity.setMenuState(p.getMenuState());
            menuEntity.setMenuPic(p.getMenuPic());
            result = menuRepository.save(menuEntity);
        } catch (Exception e){
            throw new RuntimeException("");
        }

        return result.getMenuPk();
    }

    public int delMenu(long menuPk){
        Long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        MenuEntity menuEntity = menuRepository.getReferenceById(menuPk);

        if (restaurant.getSeq() != menuEntity.getMenuResPk().getSeq()){
            throw new RuntimeException();
        }
        menuRepository.delete(menuEntity);
        return 1;
    }

}
