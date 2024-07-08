package com.green.beadalyo.lhn;

import com.green.beadalyo.common.CustomFileUtils;
import com.green.beadalyo.lhn.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewMapper mapper;
    private final ReviewFilter filter;
    private final CustomFileUtils fileUtils;

    // 리뷰 작성
    @Transactional
    public long postReview(ReviewPostReq p, List<MultipartFile> pics) {
        if (mapper.ReviewExistForOrder(p.getDoneOrderPk())) {
            log.warn("주문에 대한 리뷰가 이미 존재합니다");
            return -1;
        }

        for (int i = 0; i < filter.getPROFANITIES().length; i++) {
            if (-1 != toString().indexOf(filter.getPROFANITIES()[i])) {
                throw new RuntimeException("댓글에 비속어가 존재합니다");
            }
        }
        if (pics.size() > 4) {
            throw new RuntimeException("파일 개수는 4개 까지만 가능합니다");
        }

        if (pics == null || pics.isEmpty()) {
            p.setReviewPics1(null);
            p.setReviewPics2(null);
            p.setReviewPics3(null);
            p.setReviewPics4(null);
        } else {
            String[] picNames = new String[4];
            for (int i = 0; i < pics.size(); i++) {
                MultipartFile file = pics.get(i);
                String picName = fileUtils.makeRandomFileName(file.getOriginalFilename());
                picNames[i] = picName;
                try {
                    String folderPath = fileUtils.makeFolder("reviews");
                    String targetPath = folderPath + "/" + picName;
                    fileUtils.transferTo(file, targetPath);
                } catch (Exception e) {
                    log.error("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
                    return -1;
                }
            }
            p.setReviewPics1(picNames[0]);
            p.setReviewPics2(picNames[1]);
            p.setReviewPics3(picNames[2]);
            p.setReviewPics4(picNames[3]);
        }
        if (p.getReviewRating() < 1 || p.getReviewRating() > 5) {
            throw new RuntimeException("별점은 1에서 5까지가 최대");
        }


        mapper.insertReview(p);
        return p.getUserPk();

    }

    // 사장님 리뷰 답글
    public long postReviewReply(ReviewReplyReq p) {
        for (int i = 0; i < filter.getPROFANITIES().length; i++) {
            if (-1 != toString().indexOf(filter.getPROFANITIES()[i])) {
                throw new RuntimeException("답글에 비속어가 존재합니다");
            }

        }
        mapper.postReviewReply(p);
        return p.getReviewCommentPk();


    }
    public ReviewGetRes getReview(long reviewPk) {
        return mapper.getReview(reviewPk);
    }


    //  리뷰 불러오기
    public List<ReviewGetRes> getReviewList(ReviewGetReq req) {
        List<ReviewGetRes> reviews = mapper.getReviewList(req);
        for (ReviewGetRes review : reviews) {
            ReviewReplyRes res = mapper.getReviewComment(review.getReviewPk());
            review.setReply(res);
        }
        return reviews;
    }

    // 리뷰 업데이트 하기
    @Transactional
    public long updReview(ReviewPutReq p, List<MultipartFile> pics) {

        long reviewUserPk = mapper.getReviewUserPk(p.getReviewPk());

        if (reviewUserPk != p.getUserPk()) {
            throw new RuntimeException("리뷰를 작성한 사용자가 아닙니다");
        }

        for (int i = 0; i < filter.getPROFANITIES().length; i++) {
            if (-1 != toString().indexOf(filter.getPROFANITIES()[i])) {
                throw new RuntimeException("답글에 비속어가 존재합니다");
            }
        }
        if (p.getReviewRating() < 1 || p.getReviewRating() > 5) {
            throw new RuntimeException("별점은 1에서 5까지가 최대");
        }
        if (pics.size() > 4) {
            throw new RuntimeException("파일 개수는 4개 까지만 가능합니다");
        }
        if (p.getRemovePics() != null) {
            for (int i = 0; i < p.getRemovePics().size(); i++) {
            }
            fileUtils.deleteFiles( p.getRemovePics(), fileUtils.getUploadPath() + "reviews");

        }
        if (p.getFiles() != null) {
            String folderPath = String.format("%s/%d", "review", p.getReviewPk());
            fileUtils.makeFolder(folderPath);
            for (MultipartFile file : p.getFiles()) {
                if (!file.isEmpty()) {
                    try {
                        String targetPath = folderPath + "/" + fileUtils.makeRandomFileName(file);
                        fileUtils.transferTo(file, targetPath);
                    } catch (Exception e) {
                        log.error("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
                        return -1;
                    }
                }
            }
        }

        mapper.putReview(p); // 수정 완료
        return 1;
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(long reviewPk, long userPk)  {
        ReviewGetRes review = mapper.getReview(reviewPk);
        long reviewUserPk = ( review == null ? 0 : review.getUserPk());
        if(reviewUserPk == 0) {
            throw new NullPointerException("존재하지 않는 리뷰입니다");
        }
        if (reviewUserPk != userPk) {
            throw new RuntimeException("리뷰를 작성한 사용자가 아닙니다");
        }
       mapper.deleteReview(reviewPk, 2); // 리뷰 상태를 삭제됨(2)으로 업데이트
    }
}



