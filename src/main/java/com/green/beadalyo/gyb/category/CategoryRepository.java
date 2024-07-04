package com.green.beadalyo.gyb.category;

import com.green.beadalyo.gyb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>
{
}
