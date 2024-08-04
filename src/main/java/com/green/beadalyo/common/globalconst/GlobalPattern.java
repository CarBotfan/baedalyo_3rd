package com.green.beadalyo.common.globalconst;

public class GlobalPattern {
    public static final String userIdPattern = "^.{8,}$";
    public static final String userPwPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()-_=+\\\\\\\\|\\\\[{\\\\]};:'\\\",<.>/?]).{8,}$";
    public static final String userPicPattern = "^\\d{3}-\\d{4}-\\d{4}$";
    public static final String userEmailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
}
