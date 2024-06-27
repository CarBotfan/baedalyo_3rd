package com.green.beadalyo.jhw.security;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyUser {
    private long userId;
    private String role;
}
