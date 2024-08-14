package com.green.beadalyo.kdh.menu;


import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.menucategory.MenuCategoryRepository;
import com.green.beadalyo.jhw.menucategory.model.*;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menu.model.*;
import com.green.beadalyo.kdh.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {
    private final CustomFileUtils customFileUtils;
    private final AuthenticationFacade authenticationFacade;

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final com.green.beadalyo.kdh.menuoption.MenuOptionRepository menuOptionRepository;

    //메뉴 등록하기
    @Transactional
    public Long postMenu(MenuEntity menuEntity,String filename){
        Long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        MenuCategory menuCat = menuCategoryRepository.findByMenuCategoryPkAndRestaurant(menuEntity.getMenuCategory().getMenuCategoryPk(), restaurant);
       //로그인한 유저 사장님인지 체크
        if (restaurant == null){
            throw new RuntimeException();
        }
        menuEntity.setMenuCategory(menuCat);

        //메뉴 이름 중복체크
        Collection<Integer> stateCol = List.of(1, 2);
        if  (menuRepository.existsByMenuNameAndMenuCategoryAndMenuStateIn(menuEntity.getMenuName(), menuCat, stateCol)){
            throw new IllegalArgumentException();
        }

        //파일이 있는지 없는지 체크
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
            customFileUtils.transferTo(pic,menuEntity.getMenuPic());
        } catch (Exception e){
            e.printStackTrace();
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
        }
    }
    //메뉴 수정하기
    @Transactional
    public Long putMenu(MenuEntity menuEntity,String filename){
        Long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));

        //로그인한 유저 사장님인지 체크
        if (restaurant == null){
            throw new RuntimeException();
        }

        //메뉴 이름 중복체크
        List<GetAllMenuNames> menuName = menuRepository.findMenuNameByMenuResPkAndMenuPk(restaurant.getSeq(),menuEntity.getMenuPk());
        for (GetAllMenuNames menu : menuName) {
            if (menu.getMenuNames().equals(menuEntity.getMenuName())) {
                throw new IllegalArgumentException();
            }
        }

        //파일이 있는지 없는지 체크
        if (filename != null && !filename.isEmpty()) {
            menuEntity.setMenuPic(filename);
        }
        MenuEntity menuEntity1 =  menuRepository.save(menuEntity);

        return menuEntity1.getMenuPk();
    }

    public MenuEntity makeMenuEntityForPost(PostMenuReq p){
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setMenuContent(p.getMenuContent());
        menuEntity.setMenuName(p.getMenuName());
        menuEntity.setMenuPrice(p.getMenuPrice());
        menuEntity.setMenuState(p.getMenuState());
        return menuEntity;
    }

    public MenuEntity makeMenuEntityForPut(PutMenuReq p){
        MenuEntity menuEntity = menuRepository.getReferenceById(p.getMenuPk());
        menuEntity.setMenuContent(p.getMenuContent());
        menuEntity.setMenuName(p.getMenuName());
        menuEntity.setMenuPrice(p.getMenuPrice());
        menuEntity.setMenuState(p.getMenuState());
        return menuEntity;
    }


    //사진 수정하기
    @Transactional
    public void putPic(MenuEntity menuEntity, MultipartFile pic){

        String path = String.format("menu/%d", menuEntity.getMenuPk());
        menuEntity.setMenuPic(path + "/"+ menuEntity.getMenuPic());

        menuRepository.updateMenuPic(menuEntity.getMenuPic(), menuEntity.getMenuPk());
        try {
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
            customFileUtils.makeFolder(path);
            customFileUtils.transferTo(pic,menuEntity.getMenuPic());
        } catch (Exception e){
            e.printStackTrace();
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
        }
    }



    //메뉴 전부 불러오기
    public List<GetAllMenuResInterface> getAllMenuByUserPk(){
        long userPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(userPk));
        if (restaurant == null || restaurant.getSeq() == 0 ){
            throw new RuntimeException();
        }
        return menuRepository.findAllByMenuResPk(restaurant.getSeq());
    }

    //특정 메뉴 pk 로 메뉴 데이터 불러오기
    public List<MenuEntity> getInMenuData(List<Long> menuIds)
    {
        return menuRepository.findByMenuPkIn(menuIds);
    }


    @Transactional
    public int delMenu(Long menuPk){
        Long resUserPk = authenticationFacade.getLoginUserPk();
        Restaurant restaurant = restaurantRepository.findRestaurantByUser(userRepository.getReferenceById(resUserPk));
        if (restaurant == null){
            throw new RuntimeException();
        }
        MenuEntity menu = menuRepository.getReferenceById(menuPk);
        menu.setMenuState(3);
        return 1;
    }

    @Transactional
    public int patchCategory(MenuPatchCategoryDto dto) {
        if(!menuRepository.existsByMenuPkAndMenuCategory_Restaurant(dto.getMenuPk(), dto.getRestaurant()) || !menuCategoryRepository.existsByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), dto.getRestaurant())){
            throw new RuntimeException("DB 정보 조회 실패");
        }
        MenuEntity menu = menuRepository.findByMenuPkAndMenuCategory_Restaurant(dto.getMenuPk(), dto.getRestaurant());
        MenuCategory menuCat = menuCategoryRepository.findByMenuCategoryPkAndRestaurant(dto.getMenuCatPk(), dto.getRestaurant());
        menu.setMenuCategory(menuCat);
        return 1;
    }

    public List<MenuListGetRes> getMenuList(Restaurant res) {
        List<MenuCategory> menuCatList = menuCategoryRepository.findMenuCategoriesByRestaurantOrderByPosition(res);
        List<MenuEntity> menuList = menuRepository.findByMenuResPk(res);
        List<MenuListGetRes> result = new ArrayList<>();
        List<GetAllMenuRes> noneCatMenuList = new ArrayList<>();
        for (MenuCategory menuCategory : menuCatList) {
            MenuListGetRes menuListGetRes = new MenuListGetRes();
            menuListGetRes.setMenuCategory(new MenuCatDto(menuCategory));
            List<GetAllMenuRes> menuDtoList = new ArrayList<>();
            for (MenuEntity menu : menuList) {
                if (menu.getMenuCategory() != null && menu.getMenuCategory().equals(menuCategory)) {
                    menuDtoList.add(new GetAllMenuRes(menu));
                }
            }
            menuListGetRes.setMenu(menuDtoList);
            result.add(menuListGetRes);
        }
        for(MenuEntity menu : menuList) {
            if(menu.getMenuCategory() == null) {
                noneCatMenuList.add(new GetAllMenuRes(menu));
            }
        }
        MenuListGetRes menuListGetResNoneCat = new MenuListGetRes();
        menuListGetResNoneCat.setMenuCategory(null);
        menuListGetResNoneCat.setMenu(noneCatMenuList);
        result.add(menuListGetResNoneCat);
        return result;
    }

    public MenuEntity getMenuByOptionPk(Long optionPk) {return menuRepository.getReferenceById(menuOptionRepository.getReferenceById(optionPk).getMenu().getMenuPk());}
    public MenuEntity getMenuByMenuPk(Long menuPk) {return menuRepository.getReferenceById(menuPk);}
}

