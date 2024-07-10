package com.green.beadalyo.jhw.security;

import lombok.Getter;

@Getter
public enum SignInProviderType {
      GOOGLE(2)
    , NAVER(3)
    , KAKAO(4)
    , LOCAL(1) ;
    private int value;
  SignInProviderType(Integer i)
  {
      this.value = i;
  }
}
