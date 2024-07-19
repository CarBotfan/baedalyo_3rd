package com.green.beadalyo.kdh.menu;


import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.kdh.menu.model.*;
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
    @Transactional
    public PostMenuRes postMenu(PostMenuReq p,
                                MultipartFile pic){

        p.setResUserPk(authenticationFacade.getLoginUserPk());
        Long resPk = mapper.checkMenuResPkByResUserPk(p.getResUserPk());
        if (resPk == null) {
            throw new RuntimeException();
        }
        p.setMenuResPk(resPk);

        List<String> menuName = mapper.getMenuName(p.getMenuResPk());
        for (String menu : menuName){
            if (menu.equals(p.getMenuName())){
                throw new IllegalArgumentException();
            }
        }
        if (pic==null || pic.isEmpty()){
            p.setMenuPic(null);
            mapper.postMenu(p);
            PostMenuRes result = PostMenuRes.builder()
                    .menuPk(p.getMenuPk())
                    .menuResPk(p.getMenuResPk())
                    .menuName(p.getMenuName())
                    .menuContent(p.getMenuContent())
                    .menuPrice(p.getMenuPrice())
                    .menuPic(null)
                    .menuState(p.getMenuState())
                    .build();
            return result;
        }
        if(pic.getSize() > maxSize){
            throw new ArithmeticException();
        }
        String picName = customFileUtils.makeRandomFileName(pic);
        mapper.postMenu(p);
        String path = String.format("menu/%d", p.getMenuPk());
        p.setMenuPic(path + "/"+ picName);
        mapper.postMenuPic(p);


        try {
            customFileUtils.makeFolder(path);
            String target = String.format("%s/%s",path,picName);
            customFileUtils.transferTo(pic,target);
        } catch (Exception e){
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
        }

        PostMenuRes result = PostMenuRes.builder()
                .menuPk(p.getMenuPk())
                .menuResPk(p.getMenuResPk())
                .menuName(p.getMenuName())
                .menuContent(p.getMenuContent())
                .menuPrice(p.getMenuPrice())
                .menuPic(p.getMenuPic())
                .menuState(p.getMenuState())
                .build();
        return result;
    }

    public List<GetAllMenuRes> getAllMenu(GetAllMenuReq p){
        return mapper.getAllMenu(p);
    }

    public List<GetAllMenuRes> getAllMenuByUserPk(){
        long userPk = authenticationFacade.getLoginUserPk();
        Long resPk = mapper.checkResPkByResUserPk(userPk);
        if (resPk == null || resPk == 0 ){
            throw new NullPointerException();
        }
        GetAllMenuReq p = new GetAllMenuReq(resPk);
        return mapper.getAllMenu(p);
    }

    @Transactional
    public PutMenuRes putMenu(MultipartFile pic, PutMenuReq p){

            long userPk = authenticationFacade.getLoginUserPk();
            Long resUserPk = mapper.checkResUserPkByMenuPk(p.getMenuPk());
            log.info("asdfasdf : {}", userPk);
            log.info("asdfasdf : {}", resUserPk);

            if (resUserPk != userPk){
                throw new RuntimeException();
            }

            long menuResPk = mapper.getMenuResPkByMenuPk(p.getMenuPk());
            List<String> menuName = mapper.getMenuNameByPut(menuResPk,p.getMenuPk());
            for (String menu : menuName){
                if (menu.equals(p.getMenuName())){
                    throw new IllegalArgumentException();
                }
            }

            GetOneMenuReq req = new GetOneMenuReq(p.getMenuPk());
            GetOneMenuRes originalMenu = mapper.getOneMenu(req);
        if (pic == null || pic.isEmpty() ){
            p.setMenuPic(originalMenu.getMenuPic());
            mapper.putMenu(p);
            GetOneMenuRes afterMenu = mapper.getOneMenu(req);
            PutMenuRes result = PutMenuRes.builder()
                    .menuPk(afterMenu.getMenuPk())
                    .menuResPk(afterMenu.getMenuResPk())
                    .menuName(afterMenu.getMenuName())
                    .menuContent(afterMenu.getMenuContent())
                    .menuPrice(afterMenu.getMenuPrice())
                    .menuPic(originalMenu.getMenuPic())
                    .menuState(afterMenu.getMenuState())
                    .createdAt(afterMenu.getCreatedAt())
                    .updatedAt(afterMenu.getUpdatedAt())
                    .build();
            return result;
        }
        if(pic.getSize() > maxSize){
            throw new ArithmeticException();
        }
        try {
            String path = String.format("menu/%d",p.getMenuPk());
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
            customFileUtils.makeFolder(path);
            String picName = customFileUtils.makeRandomFileName(pic);
            String target = String.format("%s/%s",path,picName);
            customFileUtils.transferTo(pic,target);
            p.setMenuPic(path + "/" + picName);
            mapper.putMenu(p);
        } catch (Exception e){
            throw new RuntimeException("");
        }
        GetOneMenuRes afterMenu = mapper.getOneMenu(req);
        PutMenuRes result = PutMenuRes.builder()
                .menuPk(afterMenu.getMenuPk())
                .menuResPk(afterMenu.getMenuResPk())
                .menuName(afterMenu.getMenuName())
                .menuContent(afterMenu.getMenuContent())
                .menuPrice(afterMenu.getMenuPrice())
                .menuPic(afterMenu.getMenuPic())
                .menuState(afterMenu.getMenuState())
                .createdAt(afterMenu.getCreatedAt())
                .updatedAt(afterMenu.getUpdatedAt())
                .build();
        return result;
    }

    public int delMenu(long menuPk){
        Long resUserPk = authenticationFacade.getLoginUserPk();
        Long menuResPk = mapper.getMenuResPkByResUserPk(resUserPk);
        int result = mapper.delMenu(menuPk, menuResPk);
        if (menuResPk == null || menuResPk == 0 || result == 0){
            throw new RuntimeException();
        }

        return result;
    }

}
