package com.green.beadalyo.kdh.menu;


import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository;
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

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;


    //메뉴 등록하기
    @Transactional
    public Long postMenu(MenuEntity menuEntity,String filename){
        if (filename != null && !filename.isEmpty()) {
            menuEntity.setMenuPic(filename);
        }
        MenuEntity menuEntity1 =  menuRepository.save(menuEntity);
        return menuEntity1.getMenuPk();
    }
    //사진 등록하기
    @Transactional
    public void postPic(MenuEntity menuEntity, MultipartFile pic){

        String path = String.format("menu/%d", menuEntity.getMenuPk());
        menuEntity.setMenuPic(path + "/"+ menuEntity.getMenuPic());
        menuRepository.updateMenuPic(menuEntity.getMenuPic(), menuEntity.getMenuPk());
        try {
            customFileUtils.makeFolder(path);
            String target = String.format("%s/%s",path,menuEntity.getMenuPic());
            customFileUtils.transferTo(pic,target);
        } catch (Exception e){
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
        }
    }

    //메뉴 전부 불러오기
    public List<GetAllMenuResInterface> getAllMenuByUserPk(Long menuResPk){
        return menuRepository.findAllByMenuResPk(menuResPk);
    }


//    @Transactional
//    public long putMenu(MultipartFile pic, PutMenuReq p){
//
//            long userPk = authenticationFacade.getLoginUserPk();
//            Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(userPk));
//            MenuEntity menuEntity = menuRepository.getReferenceById(p.getMenuPk());
//
//            if (restaurant.getSeq() != menuEntity.getMenuResPk().getSeq()){
//                throw new RuntimeException();
//            }
//
//            long menuResPk = mapper.getMenuResPkByMenuPk(p.getMenuPk());
//            List<String> menuName = mapper.getMenuNameByPut(menuResPk,p.getMenuPk());
//            for (String menu : menuName){
//                if (menu.equals(p.getMenuName())){
//                    throw new IllegalArgumentException();
//                }
//            }
//
//        if (pic == null || pic.isEmpty() ){
//            menuEntity.setMenuContent(p.getMenuContent());
//            menuEntity.setMenuName(p.getMenuName());
//            menuEntity.setMenuPrice(p.getMenuPrice());
//            menuEntity.setMenuState(p.getMenuState());
//            menuEntity.setMenuPic(null);
//            MenuEntity result = menuRepository.save(menuEntity);
//
//            return result.getMenuPk();
//        }
//        if(pic.getSize() > maxSize){
//            throw new ArithmeticException();
//        }
//        MenuEntity result = null;
//        try {
//            String path = String.format("menu/%d",p.getMenuPk());
//            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
//            customFileUtils.makeFolder(path);
//            String picName = customFileUtils.makeRandomFileName(pic);
//            String target = String.format("%s/%s",path,picName);
//            customFileUtils.transferTo(pic,target);
//            p.setMenuPic(path + "/" + picName);
//            menuEntity.setMenuContent(p.getMenuContent());
//            menuEntity.setMenuName(p.getMenuName());
//            menuEntity.setMenuPrice(p.getMenuPrice());
//            menuEntity.setMenuState(p.getMenuState());
//            menuEntity.setMenuPic(p.getMenuPic());
//            result = menuRepository.save(menuEntity);
//        } catch (Exception e){
//            throw new RuntimeException("");
//        }
//
//        return result.getMenuPk();
//    }

    public int delMenu(MenuEntity menuEntity){
        menuRepository.delete(menuEntity);
        return 1;
    }

}

