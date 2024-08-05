package com.green.beadalyo.kdh.menuOption;

import com.green.beadalyo.kdh.menuOption.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long>
{
    List<MenuOption> findbySeqIn(List<Long> seqs);
}
