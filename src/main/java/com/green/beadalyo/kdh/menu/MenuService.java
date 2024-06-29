package com.green.beadalyo.kdh.menu;


import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.kdh.menu.model.*;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionReq;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuMapper mapper;
    private final CustomFileUtils customFileUtils;
    @Transactional
    public PostMenuRes postMenu(PostMenuReq p,
                                MultipartFile menuPic){


            if (menuPic==null || menuPic.isEmpty()){
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
            String picName = customFileUtils.makeRandomFileName(menuPic);
            p.setMenuPic(picName);
            mapper.postMenu(p);
            String path = String.format("menu/%d",p.getMenuPk());

        try {

            customFileUtils.makeFolder(path);
            String target = String.format("%s/%s",path,picName);
            customFileUtils.transferTo(menuPic,target);
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


    @Transactional
    public PutMenuRes putMenu(MultipartFile menuPic, PutMenuReq p){

            GetOneMenuReq req = new GetOneMenuReq(p.getMenuPk());
            GetOneMenuRes originalMenu = mapper.getOneMenu(req);
        if (menuPic == null || menuPic.isEmpty() ){
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
        try {
            String path = String.format("menu/%d",p.getMenuPk());
            customFileUtils.deleteFolder(customFileUtils.uploadPath + path);
            customFileUtils.makeFolder(path);
            String picName = customFileUtils.makeRandomFileName(menuPic);
            String target = String.format("%s/%s",path,picName);
            customFileUtils.transferTo(menuPic,target);
            p.setMenuPic(picName);
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
        int result = mapper.delMenu(menuPk);
        return result;
    }

}
