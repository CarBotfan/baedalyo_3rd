package com.green.beadalyo.lhn;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserService;
import com.green.beadalyo.lhn.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/rev")
@Tag(name = "리뷰 CRUD")
public class ReviewController {
    private final ReviewService service;
    private final AuthenticationFacade facade ;
    private final UserService userService ;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "고객리뷰작성")
    @ApiResponse(
            description = "<p> code : 1  => 리뷰 작성 완료 </p>"+
                    "<p> code : -1  => 주문에 대한 리뷰가 이미 존재합니다 </p>" +
                    "<p> code : -2  => 댓글에 비속어가 존재합니다 </p>"+
                    "<p> code : -3  => 파일 개수는 4개 까지만 가능합니다 </p>" +
                    "<p> code : -4  =>  별점은 1에서 5까지가 최대</p>" +
                    "<p> code : -5  =>  파일 저장 중 오류 발생:  + file.getOriginalFilename(), e</p>"
    )
    public ResultDto<Long> postReview( @RequestPart ReviewPostReq p,
                                       @RequestPart(required = false) List<MultipartFile> pics) {
        log.info("list size : {}", pics);
        int code = 1;
        String msg = "작성 완료";
        long result = 0;
        try{
            result = service.postReview(p,pics);
        }
        catch (IllegalArgumentException illegalArgumentException){
            code = -1;
            msg = illegalArgumentException.getMessage();
        }
        catch (ArithmeticException arithmeticException){
            code = -2;
            msg = arithmeticException.getMessage();
        }
        catch (NullPointerException nullPointerException){
            code = -3;
            msg = nullPointerException.getMessage();
        }
        catch (RuntimeException runtimeException){
            code = -4;
            msg = runtimeException.getMessage();
        }
        catch (Exception e){
            code = -5;
            msg = e.getMessage();
        }
        return ResultDto.<Long>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
    @PostMapping("owner/comment")
    @Operation(summary = "사장님 답글", description = "")
    public ResultDto<Long> postReviewReply(@RequestBody ReviewReplyReq p) {

        int code = 1;
        String msg = "답변 완료";
        long result = 0;
        try {
            result = service.postReviewReply(p);
        } catch (Exception e) {
            code = -16;
            msg = e.getMessage();
        }
            return ResultDto.<Long>builder()
                    .statusCode(code)
                    .resultMsg(msg)
                    .resultData(result)
                    .build();
        }

    @GetMapping("list")
    @Operation(summary = "리뷰 리스트 불러오기", description = "")
    public ResultDto<List<ReviewGetRes>> getReviewList() {
        if (facade.getLoginUser() == null) {
            return ResultDto.<List<ReviewGetRes>>builder()
                    .statusCode(-13)
                    .resultMsg("로그인한 유저가 존재하지 않음")
                    .build();
        }

        int code = 1;
        String msg = "불러오기 완료";
        List<ReviewGetRes> result = null;

        try {
            // 로그인된 사용자의 역할에 따라 다른 메서드를 호출
            String userRole = facade.getLoginUserRole();
            if ("ROLE_OWNER".equals(userRole)) { // 사장님 계정 여부를 확인
                result = service.getOwnerReviews();
            }
            if ("ROLE_USER".equals(userRole)){
                result = service.getCustomerReviews();
            }
            if ("ROLE_ADMIN".equals(userRole)){
                throw new RuntimeException("어드민임니다");
            }

        } catch (Exception e) {
            code = -13;
            msg = e.getMessage();
        }

        return ResultDto.<List<ReviewGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
    @Transactional
    @PutMapping("owner/comment")
    @Operation(summary = "사장님 답글 수정")
    @ApiResponse(description =  "<p> code : 1  => 답글 수정 완료 </p>"+
            "<p> code : -7  => 리뷰를 작성한 사장님이 아닙니다 </p>" +
            "<p> code : -2  => 답글에 비속어가 존재합니다 </p>")
    public ResultDto<Long> updReviewReply(@RequestBody  ReviewReplyUpdReq p){
        int code = 1;
        String msg = "답글수정완료";
        long result = 0;
        System.out.println(p);
        try{
            service.UpdReviewReply(p);
        }
        catch (IllegalArgumentException illegalArgumentException){
            code = -18;
            msg = illegalArgumentException.getMessage();
        }
        catch (ArithmeticException arithmeticException){
            code = -19;
            msg = arithmeticException.getMessage();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
        return ResultDto.<Long>builder()
                .statusCode(code)
                .resultMsg(msg)
                .build();
    }

    @PutMapping()
    @Operation(summary = "리뷰수정" , description = "")
    @ApiResponse(
            description = "<p> code : 1  => 리뷰 수정 완료 </p>"+
                    "<p> code : -7  => 리뷰를 작성한 사용자가 아닙니다 </p>" +
                    "<p> code : -2  => 댓글에 비속어가 존재합니다 </p>"+
                    "<p> code : -3  => 별점은 1에서 5까지가 최대 </p>" +
                    "<p> code : -4  =>  파일 개수는 4개 까지만 가능합니다</p>" +
                    "<p> code : -5  =>  파일 저장 중 오류 발생:  + file.getOriginalFilename(), e</p>"
    )
    public ResultDto<Long> updReview(@RequestPart ReviewPutReq p , @RequestPart List<MultipartFile> pics) {
        int code = 1;
        String msg = "리뷰수정완료";
        long result = 0;
        try{
            result = service.updReview(p , pics);
        }
        catch (IllegalArgumentException illegalArgumentException){
            code = -7;
            msg = illegalArgumentException.getMessage();
        }
        catch (ArithmeticException arithmeticException){
            code = -2;
            msg = arithmeticException.getMessage();
        } catch (NullPointerException nullPointerException) {
            code = -3;
            msg = nullPointerException.getMessage();
        } catch (RuntimeException runtimeException) {
            code = -4;
            msg = runtimeException.getMessage();
        } catch (Exception e) {
            code = -5;
            msg = e.getMessage();
        }
        return ResultDto.<Long>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @DeleteMapping("{review_pk}")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다")
    @ApiResponse(
            description = "<p> code : 1  => 삭제 완료 </p>" +
                    "<p> code : -10  => 존재하지 않는 리뷰입니다 </p>" +
                    "<p> code : -11  => 리뷰를 작성한 사용자가 아닙니다 </p>"
    )
    public ResultDto<Integer> deleteReview(@PathVariable("review_pk") long reviewPk) {
        facade.getLoginUser();
        if (facade.getLoginUser() == null)
            return ResultDto.<Integer>builder()
                    .statusCode(-13)
                    .resultMsg("로그인한 유저가 존재하지않음")
                    .build();
        int code = 1;
        String msg = "삭제 완료";
        try {
            service.deleteReview(reviewPk);
        } catch (NullPointerException nullPointerException) {
            code = -10;
            msg = nullPointerException.getMessage();

        } catch (RuntimeException runtimeException) {
            code = -11;
            msg = runtimeException.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .build();
    }

    @DeleteMapping("owner/comment/{review_comment_pk}")
    @Operation(summary = "사장님 답글 삭제", description = "답글을 삭제합니다")
    public ResultDto<Integer> deleteReviewReply(@PathVariable("review_comment_pk") long reviewCommentPk) {
        long userPk = facade.getLoginUserPk();
        int code = 1;
        String msg = "삭제 완료";
        try {
            service.deleteReviewReply(reviewCommentPk);
        } catch (IllegalArgumentException illegalArgumentException) {
            code = -11;
            msg = illegalArgumentException.getMessage();
        }
      return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .build();
    }
}

