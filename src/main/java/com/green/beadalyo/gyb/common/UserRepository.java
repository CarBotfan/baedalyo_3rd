package com.green.beadalyo.gyb.common;

import com.green.beadalyo.gyb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findBySeq(Long seq);
}
