package com.green.beadalyo.kdh.menuoption;

import com.green.beadalyo.kdh.menuoption.entity.*;
import com.green.beadalyo.kdh.menuoption.model.GetMenuOptionRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long>
{
    List<MenuOption> findBySeqIn(List<Long> seqs);

    @Query(value = "SELECT new com.green.beadalyo.kdh.menuoption.model.GetMenuOptionRes" +
            "(o.seq, o.menu.menuPk, o.optionName, o.optionPrice, o.optionState, o.createdAt, o.updatedAt) " +
            "FROM MenuOption o " +
            "WHERE o.menu.menuPk = :menuPk AND o.optionState = 1 OR o.optionState = 2")
    List<GetMenuOptionRes> findMenuOptionResByMenu(Long menuPk);
}
