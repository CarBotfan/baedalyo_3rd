package com.green.beadalyo.kdh.menuOption;

import com.green.beadalyo.kdh.menuOption.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuOptionMapper {
    int postMenuOption(PostMenuOptionReq p);
    int putMenuOption(PutMenuOptionReq p);
    List<GetMenuWithOptionRes> getMenuWithOption(GetMenuWithOptionReq p);
    GetMenuOptionRes getMenuOption(GetMenuOptionReq p);
    int delMenuOption(DelMenuOptionReq p);
}
