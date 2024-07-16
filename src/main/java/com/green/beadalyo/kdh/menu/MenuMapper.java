package com.green.beadalyo.kdh.menu;

import com.green.beadalyo.kdh.menu.model.*;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionReq;
import com.green.beadalyo.kdh.menuOption.model.GetMenuWithOptionRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    int postMenu(PostMenuReq p);
    void postMenuPic(PostMenuReq p);
    List<String> getMenuName(long menuResPk);
    List<String> getMenuNameByPut(long menuResPk, long menuPk);
    long getMenuResPkByMenuPk(long menuPk);

    List<GetAllMenuRes> getAllMenu(GetAllMenuReq p);

    GetOneMenuRes getOneMenu(GetOneMenuReq p);

    int putMenu(PutMenuReq p);

    int delMenu(long menuPk, long menuResPk);

    Long checkMenuResPkByResUserPk(long resUserPk);

    Long checkResUserPkByMenuPk(long menuPk);

    Long checkResPkByResUserPk(long resUserPk);

    Long getMenuResPkByResUserPk(long resUserPk);
}
