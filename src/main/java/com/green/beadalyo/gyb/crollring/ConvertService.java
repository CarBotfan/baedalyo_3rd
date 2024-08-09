package com.green.beadalyo.gyb.crollring;

import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.payment.PaymentData;
import com.green.beadalyo.jhw.menucategory.model.MenuCategory;
import com.green.beadalyo.jhw.user.entity.User;
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
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/convert")
@RequiredArgsConstructor
public class ConvertService
{
    private final WebClient webClient = WebClient.builder().baseUrl("https://www.yogiyo.co.kr").build();

    private final ConvertDataRepository repository ;

    private final PasswordEncoder encoder ;

    @GetMapping("restaurant")
    public void convertRestaurant()
    {
        List<ConvertRestaurantBefore> list = repository.findAll() ;
        for (int i = 0; i < list.size(); i++)
        {

            String middlePart = String.format("%04d", i);
            String lastPart = middlePart;

            User user = new User();
            user.setUserId("test_owner_"+i);
            user.setUserPw(encoder.encode("test1234"));
            user.setUserName("오너_"+i);
            user.setUserNickname("오너_닉네임_"+i);
            user.setUserPhone("010-"+middlePart+"-"+lastPart);
            user.setMileage(0);
            user.setUserRole("ROLE_OWNER");
            user.setUserState(1);
            user.setUserLoginType(1);
            user.setUserEmail("test_owner_"+i+"@email.com");
            ConvertRestaurantBefore crb = list.get(i) ;
            RestaurantAddData addData = getRestaurant(crb.getId()) ;

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

            new MenuCategory()

        }
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
