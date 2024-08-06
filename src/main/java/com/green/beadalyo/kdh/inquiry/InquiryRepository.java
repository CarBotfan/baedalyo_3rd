package com.green.beadalyo.kdh.inquiry;

import com.green.beadalyo.kdh.inquiry.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long> {
}
