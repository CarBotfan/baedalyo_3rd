package com.green.beadalyo.lhn.Review;

import com.green.beadalyo.lhn.Review.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    Optional<ReviewComment> findReviewCommentByReviewCommentPk(Long ReviewCommentPk);

}
