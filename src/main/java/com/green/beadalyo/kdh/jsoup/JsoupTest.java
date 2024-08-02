package com.green.beadalyo.kdh.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupTest {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://www.yogiyo.co.kr/mobile" +
                    "/#/%EB%8C%80%EA%B5%AC%EA%B4%91%EC%97%AD%EC%8B%9C/000000/%EC%B9%98%ED%82%A8/").get();
            System.out.println("이거이ㅓㄱㅁ니아ㅡㄴㅁ이ㅏ름ㄴㅇ라ㅣ" + doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
