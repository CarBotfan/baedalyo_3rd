package com.green.beadalyo.lhn.repository;

import com.green.beadalyo.lhn.entity.Review;
import com.green.beadalyo.lhn.entity.ReviewComment;
import com.green.beadalyo.lhn.model.ReviewPostReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 리뷰 작성
    @Query(nativeQuery = true, value = "INSERT INTO review (review_pk, done_order_pk, user_pk, res_pk, review_contents, review_rating, review_pics_1, review_pics_2, review_pics_3, review_pics_4) VALUES (:reviewPk, :doneOrderPk, :userPk, (SELECT res_pk FROM done_order WHERE done_order_pk = :p.doneOrderPk), :p.reviewContents, :p.reviewRating, :p.reviewPics1, :p.reviewPics2, :p.reviewPics3, :p.reviewPics4)")
    void insertReview(@Param("p") ReviewPostReq p);

    // 사장님 답글
    @Query(nativeQuery = true, value = "INSERT INTO review_comment (review_pk, comment_content) VALUES (:p.reviewPk, :p.commentContent)")
    Long postReviewReply(@Param("reviewPk") Long reviewPk,
                         @Param("commentContent") String commentContent);

    // 사장님 답글 삭제
    @Query(nativeQuery = true, value = "DELETE FROM review_comment WHERE review_comment_pk = :commentPk")
    void deleteReviewReply(@Param("commentPk") Long commentPk);

    // 리뷰 삭제
    @Query(nativeQuery = true, value = "UPDATE review SET review_state = :reviewState WHERE review_pk = :reviewPk")
    void deleteReview(@Param("reviewPk") Long reviewPk, @Param("reviewState") Integer reviewState);

    // 리뷰 리스트 불러오기
    @Query(nativeQuery = true, value = "SELECT review_pk AS reviewPk, user_pk AS userPk, review_contents AS reviewContents, review_rating AS reviewRating, review_state AS reviewState, updated_at AS updatedAt FROM review WHERE res_pk = :resPk AND review_state = 1")
    List<Object[]> getReviewList(@Param("resPk") Long resPk);

    // 리뷰 답글 가져오기
    @Query(nativeQuery = true, value = "SELECT review_pk AS reviewPk, review_comment_pk AS reviewCommentPk, comment_content AS commentContents FROM review_comment WHERE review_pk = :reviewPk")
    List<Object[]> getReviewComment(@Param("reviewPk") Long reviewPk);

    // 사장님 레스토랑 조회
    @Query(nativeQuery = true, value = "SELECT res_user_pk FROM restaurant WHERE res_pk = (SELECT res_pk FROM review WHERE review_pk = :reviewPk)")
    Long getRestaurantUser(@Param("reviewPk") Long reviewPk);

    // 유저의 레스토랑 조회
    @Query(nativeQuery = true, value = "SELECT res_pk FROM restaurant WHERE res_user_pk = :userPk")
    Long getResPkByUserPk(@Param("userPk") Long userPk);

    // 리뷰 댓글의 레스토랑 조회
    @Query(nativeQuery = true, value = "SELECT res_pk FROM review WHERE review_pk = (SELECT review_pk FROM review_comment WHERE review_comment_pk = :reviewCommentPk)")
    Long getResPkByReviewCommentPk(@Param("reviewCommentPk") Long reviewCommentPk);

    // 사장님 답글 수정
    @Query(nativeQuery = true, value = "UPDATE review_comment SET comment_content = :commentContent WHERE review_comment_pk = :reviewCommentPk")
    void updReviewReply(@Param("reviewCommentPk") Long reviewCommentPk, @Param("commentContent") String commentContent);

    // 리뷰 수정
    @Query(nativeQuery = true, value = "UPDATE review SET review_title = :reviewTitle, review_content = :reviewContent, review_rating = :reviewRating, review_pics1 = :reviewPics1, review_pics2 = :reviewPics2, review_pics3 = :reviewPics3, review_pics4 = :reviewPics4 WHERE review_pk = :reviewPk AND review_state = 1")
    void putReview(@Param("reviewPk") Long reviewPk,
                   @Param("reviewTitle") String reviewTitle,
                   @Param("reviewContent") String reviewContent,
                   @Param("reviewRating") Integer reviewRating,
                   @Param("reviewPics1") String reviewPics1,
                   @Param("reviewPics2") String reviewPics2,
                   @Param("reviewPics3") String reviewPics3,
                   @Param("reviewPics4") String reviewPics4);

    // 주문에 대한 리뷰 존재 여부 확인
    @Query(nativeQuery = true, value = "SELECT COUNT(*) > 0 FROM review WHERE done_order_pk = :orderPk AND review_state = 1")
    boolean ReviewExistForOrder(@Param("orderPk") Long orderPk);

    // 리뷰 작성자 확인
    @Query(nativeQuery = true, value = "SELECT user_pk FROM review WHERE review_pk = :reviewPk AND review_state = 1")
    Long getReviewUserPk(@Param("reviewPk") Long reviewPk);

    // 특정 리뷰 조회
    @Query(nativeQuery = true, value = "SELECT review_pk AS reviewPk, user_pk AS userPk, review_contents AS reviewContents, review_rating AS reviewRating, review_state AS reviewState, updated_at AS updatedAt FROM review WHERE review_pk = :reviewPk AND review_state = 1")
    List<Object[]> getReview(@Param("reviewPk") Long reviewPk);

    // 사용자 권한 조회
    @Query(nativeQuery = true, value = "SELECT user_role FROM user WHERE user_pk = :userPk")
    String getUserRole(@Param("userPk") Long userPk);

    // 특정 레스토랑의 리뷰 조회
    @Query(nativeQuery = true, value = "SELECT review_pk AS reviewPk, user_pk AS userPk, review_contents AS reviewContents, review_rating AS reviewRating, review_state AS reviewState, updated_at AS updatedAt, created_at AS createdAt, review_pics_1 AS reviewPics1, review_pics_2 AS reviewPics2, review_pics_3 AS reviewPics3, review_pics_4 AS reviewPics4 FROM review WHERE res_pk = :resPk AND review_state = 1 ORDER BY created_at DESC")
    List<Object[]> getReviewsRestaurant(@Param("resPk") Long resPk);

    // 특정 사용자의 리뷰 조회
    @Query(nativeQuery = true, value = "SELECT A.review_pk AS reviewPk, A.user_pk AS userPk, A.review_contents AS reviewContents, A.review_rating AS reviewRating, A.review_state AS reviewState, A.updated_at AS updatedAt, A.created_at AS createdAt, A.review_pics_1 AS reviewPics1, A.review_pics_2 AS reviewPics2, A.review_pics_3 AS reviewPics3, A.review_pics_4 AS reviewPics4, B.res_name AS resName FROM review A LEFT JOIN restaurant B ON A.res_pk = B.res_pk WHERE A.user_pk = :userPk AND A.review_state = 1 ORDER BY A.created_at DESC")
    List<Object[]> getReviewsUser(@Param("userPk") Long userPk);

    // 유저 닉네임 조회
    @Query(nativeQuery = true, value = "SELECT user_nickname FROM user WHERE user_pk = :userPk")
    String selectUserNickName(@Param("userPk") Long userPk);
}