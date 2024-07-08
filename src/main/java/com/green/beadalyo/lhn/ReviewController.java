package com.green.beadalyo.lhn;

import com.green.beadalyo.common.model.ResultDto;
import com.green.beadalyo.lhn.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/review")
@Tag(name = "리뷰 CRUD")
public class ReviewController {
    private final ReviewService service;
    @PostMapping("api/review")
    @Operation(summary = "고객리뷰작성",description = "")
    public ResultDto<Long> postReview(@RequestPart ReviewPostReq p, @RequestPart(required = false) List<MultipartFile> pics) {

        int code = 2;
        String msg = "작성 완료";
        long result = 0;
        try{
            result = service.postReview(p,pics);
        }catch (Exception e){
            code = 4;
            msg = e.getMessage();
        }
        return ResultDto.<Long>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
    @PostMapping("api/comment")
    @Operation(summary = "사장님 답글", description = "")
    public ResultDto<Long> postReviewReply(@RequestBody ReviewReplyReq p) {
        int code = 2;
        String msg = "답변 완료";
        long result = 0;
        try{
            result = service.postReviewReply(p);
        }catch (Exception e){
            code = 4;
            msg = e.getMessage();
        }
        return ResultDto.<Long>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
    @GetMapping("reviewlist")
    @Operation(summary = "리뷰 리스트 불러오기", description = "")
    public ResultDto<List<ReviewGetRes>> ReviewGetRes(@ModelAttribute ReviewGetReq p){
        int code = 2;
        String msg = "불러오기 완료";
        List<ReviewGetRes> result = null;
        try{
            result = service.getReviewList(p);

        }catch (Exception e){
            code = 4;
            msg = e.getMessage();
        }
        return ResultDto.<List<ReviewGetRes>>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }

    @PutMapping("patchreview")
    @Operation(summary = "리뷰수정" , description = "")
    public ResultDto<Long> updReview(@RequestPart ReviewPutReq p , @RequestPart List<MultipartFile> pics) {
        int code = 2;
        String msg = "리뷰수정완료";
        long result = 0;
        try {
            result = service.updReview(p,pics);
        } catch (Exception e) {
            code = 4;
            msg = e.getMessage();
        }
        return ResultDto.<Long>builder()
                .statusCode(code)
                .resultMsg(msg)
                .resultData(result)
                .build();
    }
    @DeleteMapping("delete")
    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다")
    public ResultDto<Integer> deleteReview(@RequestParam long reviewPk, @RequestParam long userPk) {
        int code = 2;
        String msg = "삭제 완료";
        try {
            service.deleteReview(reviewPk, userPk);
        } catch (NullPointerException e) {
            msg = e.getMessage();
            code = 6 ;

        } catch (Exception e) {
            e.printStackTrace();
            code = 4;
            msg = e.getMessage();
        }
        return ResultDto.<Integer>builder()
                .statusCode(code)
                .resultMsg(msg)
                .build();
    }
}
