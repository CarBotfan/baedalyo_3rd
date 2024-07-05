package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.common.FileUtils;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.User;
import com.green.beadalyo.gyb.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(RestaurantService.class)
@ExtendWith(SpringExtension.class)
class RestaurantServiceTest
{

    @MockBean
    RestaurantRepository repository ;

    @Autowired RestaurantService service;

    @Test
    @DisplayName("세이브 테스트")
    void save() throws Exception
    {
        User user = getTestUser() ;
        RestaurantInsertDto dto = getTestRestaurantDto(user) ;

        Restaurant rest = new Restaurant(dto);
        service.save(rest);
    }

    @Test
    @DisplayName("카테고리 별 레스토랑 조회")
    void getRestaurantByCategory()
    {
    }

    @Test
    @DisplayName("레스토랑 상세조회 테스트")
    void getRestaurantData() throws Exception
    {
        User user = new User();
        RestaurantInsertDto dto = getTestRestaurantDto(user) ;
        given(repository.findTop1ByUser(user)).willReturn(Optional.of(new Restaurant(dto))) ;
        Restaurant rest = service.getRestaurantData(user);
        assertNotNull(rest);
        assertEquals(rest, new Restaurant(dto), "1. 리턴값 상이");

    }

    @Test
    @DisplayName("레스토랑 PK호출 테스트")
    void getRestaurantDataBySeq() throws Exception
    {

        Restaurant rest = new Restaurant(getTestRestaurantDto(getTestUser())) ;
        given(repository.findTop1BySeq(4L)).willReturn(Optional.of(new Restaurant(getTestRestaurantDto(getTestUser())))) ;
        Restaurant rest2 = service.getRestaurantDataBySeq(4L);
        assertNotNull(rest);
        assertEquals(rest,rest2 );
        System.out.println(rest);
    }

    @Test
    @DisplayName("레스토랑 데이터 insert 서비스 테스트")
    void insertRestaurantData() throws Exception
    {
        RestaurantInsertDto dto = getTestRestaurantDto(getTestUser());
        Restaurant rest = new Restaurant(dto);
//        given(repository.save(rest)).willReturn(rest);
        service.insertRestaurantData(dto);

    }

    @Test
    @DisplayName("레스토랑 데이터 삭제 테스트")
    void deleteRestaurantData() throws Exception
    {
        RestaurantInsertDto dto = getTestRestaurantDto(getTestUser());
        Restaurant rest = new Restaurant(dto);
        given(repository.findTop1ByUser(getTestUser())).willReturn(Optional.of(new Restaurant(getTestRestaurantDto(getTestUser())))) ;
//        given(repository.save(rest)).willReturn(rest);
        service.deleteRestaurantData(getTestUser());

    }

    @Test
    @DisplayName("토글 테스트")
    void toggleState() throws Exception
    {
        User user = getTestUser();
        RestaurantInsertDto dto = getTestRestaurantDto(user);
        Restaurant rest = new Restaurant(dto) ;
        rest.setState(2);
        given(repository.findTop1ByUser(user)).willReturn(Optional.of(rest));
        Integer result = service.toggleState(user) ;
        assertNotNull(result);
        assertEquals(result,1);

    }

//    @Test
//    @DisplayName("음식점 사진 변경 서비스 테스트")
//    void updateRestaurantPic() throws Exception
//    {
//        User user = getTestUser();
//        RestaurantInsertDto dto = getTestRestaurantDto(user);
//        Restaurant rest = new Restaurant(dto);
//        rest.setPic("asdf");
//
//        given(repository.findTop1ByUser(user)).willReturn(Optional.of(rest));
//        given(FileUtils.fileDelete(rest.getSeq().toString(), rest.getPic())).willReturn(true);
//        given(FileUtils.fileInput(rest.getSeq().toString(),new MultipartFile file)) ;

//    }



    private User getTestUser()
    {
        return new User() ;
    }

    private RestaurantInsertDto getTestRestaurantDto(User user)
    {
        RestaurantInsertDto dto = new RestaurantInsertDto() ;
        dto.setUser(user);
        dto.setName("맛있는 식당") ;
        dto.setCloseTime(LocalTime.of(23, 0));
        dto.setOpenTime(LocalTime.of(8,0));
        dto.setResAddr("대구 북구 대현동");
        dto.setResCoorX(123.156548581);
        dto.setResCoorY(123.156548581);
        dto.setRegiNum("123-15-12345");
        return dto ;
    }
}