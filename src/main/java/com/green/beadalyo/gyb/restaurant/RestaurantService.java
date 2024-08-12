package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.common.FileUtils;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.common.exception.DataWrongException;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.RestaurantDetailView;
import com.green.beadalyo.gyb.model.RestaurantListView;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantDetailViewRepository;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantListViewRepository;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static com.green.beadalyo.gyb.common.ConstVariable.PAGE_SIZE;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RestaurantService
{
    private final RestaurantRepository repository;

    private final RestaurantListViewRepository viewListRepository;

    private final RestaurantDetailViewRepository viewDetailRepository;

    private final FileUtils fileUtils ;
    private final RestaurantListViewRepository restaurantListViewRepository;
    private final AuthenticationFacade authenticationFacade;


    // 업데이트
    public void save(Restaurant data) throws Exception
    {
        repository.save(data);
    }

    //음식점 카테고리 기준 정보 호출
    public Page<RestaurantListView> getRestaurantByCategory(Long seq, BigDecimal x, BigDecimal y, Integer orderType, Integer page, String search ) throws Exception
    {
        Pageable pageable = PageRequest.of(page-1, PAGE_SIZE) ;
        BigDecimal range = BigDecimal.valueOf(0.09);
        BigDecimal xMin = x.subtract(range);
        BigDecimal xMax = x.add(range);
        BigDecimal yMin = y.subtract(range);
        BigDecimal yMax = y.add(range);
        try {
            Long userPk = authenticationFacade.getLoginUserPk();
            return switch (orderType)
            {
                case 1 -> seq == 0 ? viewListRepository.findAllByCategoryIdAndCoordinates(xMin,xMax,yMin,yMax,search,userPk,pageable) : viewListRepository.findByCategoryIdAndCoordinates(seq,xMin,xMax,yMin,yMax,search,userPk,pageable);
                case 2 -> seq == 0 ? viewListRepository.findALLByCategoryIdAndCoordinatesSortedByDistance(xMin,xMax,yMin,yMax,x,y,search,userPk,pageable) : viewListRepository.findByCategoryIdAndCoordinatesSortedByDistance(seq,xMin,xMax,yMin,yMax,x,y,search,userPk,pageable);
                case 3 -> seq == 0 ? viewListRepository.findAllByCategoryIdAndCoordinatesSortedByScore(xMin,xMax,yMin,yMax,search,userPk,pageable) : viewListRepository.findByCategoryIdAndCoordinatesSortedByScore(seq, xMin,xMax,yMin,yMax,search,userPk,pageable);
                default -> null ;
            };
        } catch (Exception e) {
            return switch (orderType)
            {
                case 1 -> seq == 0 ? viewListRepository.findAllByCategoryIdAndCoordinates(xMin,xMax,yMin,yMax,search,pageable) : viewListRepository.findByCategoryIdAndCoordinates(seq,xMin,xMax,yMin,yMax,search,pageable);
                case 2 -> seq == 0 ? viewListRepository.findALLByCategoryIdAndCoordinatesSortedByDistance(xMin,xMax,yMin,yMax,x,y,search,pageable) : viewListRepository.findByCategoryIdAndCoordinatesSortedByDistance(seq,xMin,xMax,yMin,yMax,x,y,search,pageable) ;
                case 3 -> seq == 0 ? viewListRepository.findAllByCategoryIdAndCoordinatesSortedByScore(xMin,xMax,yMin,yMax,search,pageable) : viewListRepository.findByCategoryIdAndCoordinatesSortedByScore(seq, xMin,xMax,yMin,yMax,search,pageable);
                default -> null ;
            };
        }
    }


    public Page<RestaurantListView> getFollowRestaurantList(Integer page ) throws Exception
    {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE) ;
        Long userPk = authenticationFacade.getLoginUserPk();

        return viewListRepository.findFollowedRestaurant(userPk, pageable);
    }


    //음식점 정보 호출
    public Restaurant getRestaurantData(User user) throws Exception
    {
        if (user == null) throw new DataNotFoundException("유저 정보를 찾을수 없습니다.") ;
        return repository.findTop1ByUser(user).orElseThrow(NullPointerException::new);
    }
    //음식점 정보 호출(PK)
    public Restaurant getRestaurantDataBySeq(Long seq) throws Exception
    {
        return repository.findTop1BySeq(seq).orElseThrow(NullPointerException::new);
    }

    //음식점 뷰 정보 호출(음식점 pk로)
    public RestaurantDetailView getRestaurantDataViewBySeq(Long seq) throws Exception
    {
        try {
            Long userPk = authenticationFacade.getLoginUserPk();
            return viewDetailRepository.findByRestaurantPk(seq, userPk).orElseThrow(NullPointerException::new);
        } catch (Exception e) {
            return viewDetailRepository.findByRestaurantPk(seq).orElseThrow(NullPointerException::new);
        }
    }

    //인서트(사업자 회원가입에서 서비스 호출 필요)
    public void insertRestaurantData(RestaurantInsertDto dto) throws Exception
    {
        String regex = "^\\d{3}-\\d{2}-\\d{5}$";
        if (!dto.getRegiNum().matches(regex))
            throw new DataWrongException("사업자 번호는 nnn-nn-nnnnn의 형식으로 들어와야 합니다.") ;
        Restaurant data = new Restaurant(dto);
        data.setState(4);
        repository.save(data);
    }

    //폐업 처리(사업자 회원 탈퇴시 호출 필요)
    public void deleteRestaurantData(User user) throws Exception
    {
        Restaurant data = repository.findTop1ByUser(user).orElseThrow(NullPointerException::new);
        data.setState(5);
        repository.save(data);
        System.out.println(data);
    }

    //음식점 상태 전환(영업 <-> 휴점)
    public Integer toggleState(User user) throws Exception
    {

        Restaurant data = repository.findTop1ByUser(user).orElseThrow(NullPointerException::new);
        if (data.getState() == 1) data.setState(2);
        else if (data.getState() == 2) data.setState(1);
        else throw new DataWrongException() ;

        repository.save(data) ;

        return data.getState() ;
    }

    //음식점 사진 변경
    public String updateRestaurantPic(User user, MultipartFile file) throws Exception
    {
        Restaurant data = repository.findTop1ByUser(user).orElseThrow(NullPointerException::new);

        if (data.getPic() != null) fileUtils.fileDelete(data.getPic()) ;
        return fileUtils.fileInput("restaurant/" + data.getSeq().toString(),file) ;


    }


}
