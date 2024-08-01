package com.green.beadalyo.jhw.MenuCategory;

import com.green.beadalyo.jhw.MenuCategory.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuCategoryService {
    private final MenuCategoryRepository repository;

}
