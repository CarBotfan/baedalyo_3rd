package com.green.beadalyo.gyb.restaurant;

import com.green.beadalyo.gyb.common.FileUtils;
import com.green.beadalyo.gyb.common.exception.DataNotFoundException;
import com.green.beadalyo.gyb.common.exception.DataWrongException;
import com.green.beadalyo.gyb.dto.RestaurantInsertDto;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.green.beadalyo.gyb.common.ConstVariable.PAGE_SIZE;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RestaurantService
{
    private final RestaurantRepository repository;

    // 업데이트
    public void save(Restaurant data) throws Exception
    {
        repository.save(data);
    }

    //음식점 카테고리 기준 정보 호출
    public Page<Restaurant> getRestaurantByCategory(Long seq, Integer page )
    {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE) ;
        return repository.findByCategoryId(seq,pageable);
    }

    //음식점 정보 호출
    public Restaurant getRestaurantData(User user) throws Exception
    {
        if (user == null) throw new DataNotFoundException("유저 정보를 찾을수 없습니다.") ;
        return repository.findTop1ByUser(user).orElseThrow(NullPointerException::new);
    }

    //음식점 정보 호출(음식점 pk로)
    public Restaurant getRestaurantDataBySeq(Long seq) throws Exception
    {
        return repository.findTop1BySeq(seq).orElseThrow(NullPointerException::new);
    }

    //인서트(사업자 회원가입에서 서비스 호출 필요)
    public void insertRestaurantData(RestaurantInsertDto dto) throws Exception
    {
        Restaurant data = new Restaurant(dto);
        repository.save(data) ;
    }

    //폐업 처리(사업자 회원 탈퇴시 호출 필요)
    public void deleteRestaurantData(User user) throws Exception
    {
        Restaurant data = repository.findTop1ByUser(user).orElseThrow(NullPointerException::new);
        data.setState(3);
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

        if (data.getPic() != null) FileUtils.fileDelete(data.getPic()) ;
        return FileUtils.fileInput(data.getSeq().toString(),file) ;


    }


}
