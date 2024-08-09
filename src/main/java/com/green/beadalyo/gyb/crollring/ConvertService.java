package com.green.beadalyo.gyb.crollring;

import com.green.beadalyo.gyb.category.CategoryRepository;
import com.green.beadalyo.gyb.model.Category;
import com.green.beadalyo.gyb.model.MatchingCategoryRestaurant;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.payment.PaymentData;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.kdh.menu.entity.MenuEntity;
import com.green.beadalyo.kdh.menuoption.entity.MenuOption;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("api/convert")
@RequiredArgsConstructor
public class ConvertService
{
    private final WebClient webClient = WebClient.builder().baseUrl("https://www.yogiyo.co.kr").build();

    private final ConvertDataRepository repository ;

    private final RestaurantRepository restaurantRepository ;

    private final PasswordEncoder encoder ;

    private final CategoryRepository categoryRepository ;

    @GetMapping("restaurant")
    public void convertRestaurant()
    {
        List<ConvertRestaurantBefore> list = repository.findAll() ;
        List<Restaurant> restaurantList = new ArrayList<>( );
        for (int i = 0; i < list.size(); i++)
        {

            String middlePart = String.format("%04d", i);


            ConvertRestaurantBefore crb = list.get(i) ;
            RestaurantAddData addData = getRestaurant(crb.getId()) ;
            List<ConvertData> convertData = getMenuData(crb.getId()) ;
            User user = new User();
            user.setUserId("test_owner_"+i);
            user.setUserPw(encoder.encode("test1234"));
            user.setUserName(addData.getCrmdata().getOwner());
            user.setUserNickname("오너_닉네임_"+i);
            user.setUserPhone(addData.getCrmdata().getPhone());
            user.setMileage(0);
            user.setUserRole("ROLE_OWNER");
            user.setUserState(1);
            user.setUserLoginType(1);
            user.setUserEmail("test_owner_"+i+"@email.com");
            Restaurant rest = new Restaurant() ;
            rest.setSeq(crb.getId());
            rest.setUser(user);
            rest.setName(crb.getName());
            rest.setRestaurantDescription(addData.getIntroduction_by_owner().getIntroduction_text());
            rest.setAddress(crb.getAddress()) ;
            rest.setRegiNum(addData.getCrmdata().getCompany_number());
            rest.setCoorX(crb.getLatitude());
            rest.setCoorY(crb.getLongitude());
            rest.setOpenTime(LocalTime.of(new Random().nextInt(24),0));
            rest.setCloseTime(LocalTime.of(new Random().nextInt(24),0));
            int random = new Random().nextInt(3)+1 ;
            rest.setState(random == 3 ? 2 : 1) ;
            rest.setPic(crb.getLogo_url());
            String ctr = crb.getCategories() ;
            ctr = ctr.replace("[","").replace("]","").replace("'","").trim() ;
            List<String> strList = Arrays.stream(ctr.split(",")).map(String::trim).toList();
            List<Category> cateList =  categoryRepository.findAll() ;
            List<Category> cateList2 =  new ArrayList<>() ;
            strList.forEach(s ->
                {
                    cateList.forEach(
                            s1 -> {
                                if (!s1.getCategoryName().equals(s)) return ;
                                cateList2.add(s1) ;
                            }
                    );
                });
            rest.setCategories(cateList2);
            List<MenuCategory> categories = new ArrayList<>();
            IntStream.range(0,convertData.size()).forEach(j ->
                {
                    ConvertData data = convertData.get(j);
                    MenuCategory category = new MenuCategory() ;
                    category.setRestaurant(rest);
                    category.setMenuCatName(data.getName());
                    category.setPosition(j+1L);
                    List<MenuEntity> menuData = new ArrayList<>() ;

                    data.getItems().forEach(
                        item ->
                        {
                            MenuEntity menu = new MenuEntity() ;
                            menu.setMenuCategory(category);
                            menu.setMenuName(item.getName());
                            menu.setMenuContent(item.getDescription());
                            menu.setMenuPrice(item.getPrice());
                            menu.setMenuPic(item.getOriginal_image());
                            menu.setMenuState(1);
                            List<MenuOption> optionList = new ArrayList<>();
                            item.getSubchoices().stream().forEach(
                                subscript ->
                                {
                                    Subchoice sub = subscript.getSubchoices().get(0); ;
                                    MenuOption menuOption = new MenuOption() ;
                                    menuOption.setMenu(menu);
                                    menuOption.setOptionName(sub.getName());
                                    menuOption.setOptionPrice(sub.getDeposit_price());
                                    menuOption.setOptionState(sub.isSoldout() ? 2 : 1);
                                    optionList.add(menuOption) ;
                                }
                            );
                            menu.setOptionList(optionList);
                            menuData.add(menu);
                        }
                    );
                    category.setMenuList(menuData);
                    categories.add(category) ;
                }
            );
            rest.setMenuList(categories);
            restaurantList.add(rest) ;

        }
            restaurantRepository.saveAll(restaurantList) ;
    }


    public List<ConvertData> getMenuData(Long id)
    {

            Mono<List<ConvertData>> getData = webClient.get()
                    .uri("/api/v1/restaurants/"+id+"/menu/?add_photo_menu=android&add_one_dish_menu=true&order_serving_type=delivery&serving_type=vd")
                    .header("X-ApiSecret","fe5183cc3dea12bd0ce299cf110a75a2")
                    .header("x-ApiKey", "iphoneap")
                    .header("Content-Type","application/x-www-form-urlencoded")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ConvertData>>() {})
                    .doOnSuccess(response -> System.out.println("성공했습니다." + response))
                    .doOnError(error -> System.out.println("실패했습니다." + error.getMessage())) ;

            return getData.block() ;

//            try {
//                data.stream().forEach(
//                        convertData ->
//                        {
//                            MenuCategory menuCategory = new MenuCategory();
//                            menuCategory.setMenuCatName(convertData.getName());
//                            menuCategory.setRestaurant();
//
//                        }
//
//                );
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }



    }

    public RestaurantAddData getRestaurant(Long id)
    {

        Mono<RestaurantAddData> getData = webClient.get()
                .uri("/api/v1/restaurants/"+id+"/info/?serving_type=vd")
                .header("X-ApiSecret","fe5183cc3dea12bd0ce299cf110a75a2")
                .header("x-ApiKey", "iphoneap")
                .header("Content-Type","application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(RestaurantAddData.class)
                .doOnSuccess(response -> System.out.println("성공했습니다." + response))
                .doOnError(error -> System.out.println("실패했습니다." + error.getMessage())) ;

        return getData.block() ;

//            try {
//                data.stream().forEach(
//                        convertData ->
//                        {
//                            MenuCategory menuCategory = new MenuCategory();
//                            menuCategory.setMenuCatName(convertData.getName());
//                            menuCategory.setRestaurant();
//
//                        }
//
//                );
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }



    }

}
