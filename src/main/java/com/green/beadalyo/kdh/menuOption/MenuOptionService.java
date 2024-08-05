package com.green.beadalyo.kdh.menuOption;

import com.green.beadalyo.kdh.menuOption.entity.MenuOption;
import com.green.beadalyo.kdh.menuOption.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuOptionService {

    final MenuOptionRepository repository ;

    public List<MenuOption> getListIn(List<Long> ids)
    {
        return repository.findBySeqIn(ids) ;
    }




//    private final MenuOptionMapper mapper;
//
//    public PostMenuOptionRes postMenuOption(PostMenuOptionReq p){
//        mapper.postMenuOption(p);
//
//        return PostMenuOptionRes.builder()
//                .optionPk(p.getOptionPk())
//                .optionMenuPk(p.getOptionMenuPk())
//                .option1Name(p.getOption1Name())
//                .option2Name(p.getOption2Name())
//                .optionPrice(p.getOptionPrice())
//                .optionState(p.getOptionState())
//                .build();
//    }
//    public List<GetMenuWithOptionRes> getMenuWithOption(GetMenuWithOptionReq p){
//
//        return mapper.getMenuWithOption(p);
//    }
//    public PutMenuOptionRes putMenuOption(PutMenuOptionReq p){
//        mapper.putMenuOption(p);
//        GetMenuOptionReq req = new GetMenuOptionReq(p.getOptionPk());
//        GetMenuOptionRes afterPutMenuOption = mapper.getMenuOption(req);
//
//
//        return PutMenuOptionRes.builder()
//                .optionPk(afterPutMenuOption.getOptionPk())
//                .optionMenuPk(afterPutMenuOption.getOptionMenuPk())
//                .option1Name(afterPutMenuOption.getOption1Name())
//                .option2Name(afterPutMenuOption.getOption2Name())
//                .optionPrice(afterPutMenuOption.getOptionPrice())
//                .optionState(afterPutMenuOption.getOptionState())
//                .createdAt(afterPutMenuOption.getCreatedAt())
//                .updatedAt(afterPutMenuOption.getUpdatedAt())
//                .build();
//    }
//
//    public int delMenuOption(DelMenuOptionReq p){
//        int affectedRow = mapper.delMenuOption(p);
//        return affectedRow;
//    }

}
