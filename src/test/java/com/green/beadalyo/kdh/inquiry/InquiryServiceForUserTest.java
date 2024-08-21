package com.green.beadalyo.kdh.inquiry;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.repository.UserRepository;
import com.green.beadalyo.kdh.inquiry.InquiryRepository;
import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import com.green.beadalyo.kdh.inquiry.user.InquiryServiceForUser;
import com.green.beadalyo.kdh.inquiry.user.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InquiryServiceForUserTest {

    @InjectMocks
    private InquiryServiceForUser inquiryServiceForUser;

    @Mock
    private InquiryRepository inquiryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByPk() {
        // Given
        Long userPk = 1L;
        User mockUser = new User();
        mockUser.setUserPk(userPk);
        when(userRepository.getReferenceById(userPk)).thenReturn(mockUser);

        // When
        User result = inquiryServiceForUser.getUserByPk(userPk);

        // Then
        assertEquals(mockUser, result);
        verify(userRepository, times(1)).getReferenceById(userPk);
    }

    @Test
    void testMakeInquiryForPost() {
        // Given
        PostInquiryForUserReq request = new PostInquiryForUserReq();
        request.setInquiryTitle("Test Title");
        request.setInquiryContent("Test Content");
        User mockUser = new User();
        request.setUser(mockUser);

        // When
        InquiryEntity result = inquiryServiceForUser.makeInquiryForPost(request);

        // Then
        assertEquals("Test Title", result.getInquiryTitle());
        assertEquals("Test Content", result.getInquiryContent());
        assertEquals(mockUser, result.getUser());
        assertEquals(1, result.getInquiryState());
    }

    @Test
    void testSaveInquiry() {
        // Given
        InquiryEntity inquiryEntity = new InquiryEntity();

        // When
        Integer result = inquiryServiceForUser.saveInquiry(inquiryEntity);

        // Then
        assertEquals(1, result);
        verify(inquiryRepository, times(1)).save(inquiryEntity);
    }

    @Test
    void testMakeInquiryForPut() {
        // Given
        PutInquiryForUserReq request = new PutInquiryForUserReq();
        request.setInquiryPk(1L);
        request.setInquiryTitle("Updated Title");
        request.setInquiryContent("Updated Content");
        User mockUser = new User();
        request.setUser(mockUser);

        InquiryEntity mockInquiryEntity = new InquiryEntity();
        mockInquiryEntity.setUser(mockUser);

        when(inquiryRepository.getReferenceById(1L)).thenReturn(mockInquiryEntity);

        // When
        InquiryEntity result = inquiryServiceForUser.makeInquiryForPut(request);

        // Then
        assertEquals("Updated Title", result.getInquiryTitle());
        assertEquals("Updated Content", result.getInquiryContent());
        assertEquals(mockUser, result.getUser());
    }

    @Test
    void testGetInquiryListForUsers() {
        // Given
        Long userPk = 1L;
        Integer page = 1;
        Pageable pageable = PageRequest.of(page - 1, 10);
        User mockUser = new User();
        List<InquiryEntity> inquiryEntities = new ArrayList<>();
        Page<InquiryEntity> mockPage = new PageImpl<>(inquiryEntities);

        when(userRepository.getReferenceById(userPk)).thenReturn(mockUser);
        when(inquiryRepository.findByUserOrderByInquiryPkDesc(mockUser, pageable)).thenReturn(mockPage);

        // When
        Page<InquiryEntity> result = inquiryServiceForUser.getInquiryListForUsers(userPk, page);

        // Then
        assertEquals(mockPage, result);
        verify(userRepository, times(1)).getReferenceById(userPk);
        verify(inquiryRepository, times(1)).findByUserOrderByInquiryPkDesc(mockUser, pageable);
    }

    @Test
    void testMakeGetInquiryListForUser() {
        // Given
        List<InquiryEntity> inquiryEntities = new ArrayList<>();
        InquiryEntity entity = new InquiryEntity();
        entity.setInquiryPk(1L);
        entity.setInquiryTitle("Test Title");
        inquiryEntities.add(entity);
        Page<InquiryEntity> mockPage = new PageImpl<>(inquiryEntities);

        // When
        List<GetInquiryListForUser> result = inquiryServiceForUser.makeGetInquiryListForUser(mockPage);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getInquiryTitle());
    }

    @Test
    void testMakeInquiryListDto() {
        // Given
        InquiryListDto dto = new InquiryListDto();
        List<GetInquiryListForUser> resultList = new ArrayList<>();

        // Mockito로 Page 인터페이스를 모킹합니다.
        Page<InquiryEntity> mockPage = mock(Page.class);

        // mockPage의 메서드 결과를 설정합니다.
        when(mockPage.getTotalPages()).thenReturn(3);
        when(mockPage.getNumberOfElements()).thenReturn(5);

        // When
        InquiryListDto result = inquiryServiceForUser.makeInquiryListDto(dto, mockPage, resultList);

        // Then
        assertEquals(3, result.getTotalPage());
        assertEquals(5, result.getTotalElements());
        assertEquals(resultList, result.getResult());
    }

    @Test
    void testCheckUser() {
        // Given
        Long inquiryPk = 1L;
        Long userPk = 2L;

        User mockUser = new User();
        mockUser.setUserPk(userPk);

        InquiryEntity mockInquiryEntity = new InquiryEntity();
        mockInquiryEntity.setUser(mockUser);

        when(authenticationFacade.getLoginUserPk()).thenReturn(userPk);
        when(inquiryRepository.getReferenceById(inquiryPk)).thenReturn(mockInquiryEntity);

        // When
        boolean result = inquiryServiceForUser.checkUser(inquiryPk);

        // Then
        assertTrue(result);
    }

    @Test
    void testDelInquiry() {
        // Given
        Long inquiryPk = 1L;
        InquiryEntity mockInquiryEntity = new InquiryEntity();
        mockInquiryEntity.setInquiryState(1);

        when(inquiryRepository.getReferenceById(inquiryPk)).thenReturn(mockInquiryEntity);

        // When
        inquiryServiceForUser.delInquiry(inquiryPk);

        // Then
        verify(inquiryRepository, times(1)).delete(mockInquiryEntity);
    }
}