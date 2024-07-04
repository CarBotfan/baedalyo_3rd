package com.green.beadalyo.gyb.category;

import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.MatchingCategoryRestaurant;
import com.green.beadalyo.gyb.model.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryService
{
    private final CategoryRepository repository ;

    private final MatchingCateforyRepository matchingRepository ;

    public List<Category> getCategoryAll()
    {
        return repository.findAll();
    }

    public Category getCategory(Long seq)
    {
        return repository.findById(seq).orElseThrow(NullPointerException::new);
    }


    public void InsertCategory(String str, String filepath) throws Exception
    {
        Category category = new Category(str, filepath) ;
        repository.save(category);
    }

    public void deleteCategory(Category cate) throws Exception
    {
        repository.delete(cate);
    }

    public void InsertRestaurantCategory(Restaurant restaurant,Category cate) throws Exception
    {
        MatchingCategoryRestaurant data = new MatchingCategoryRestaurant(restaurant,cate) ;
        matchingRepository.save(data);
    }

    public void deleteRestaurantCategory(Restaurant restaurant,Category cate) throws Exception
    {
        MatchingCategoryRestaurant data = matchingRepository.findTop1ByCategoryAndRestaurant(cate,restaurant) ;
        matchingRepository.delete(data);
    }

}
