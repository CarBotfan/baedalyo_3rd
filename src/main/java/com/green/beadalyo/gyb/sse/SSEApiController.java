package com.green.beadalyo.gyb.sse;

import ch.qos.logback.classic.Logger;
import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.security.MyUserDetails;
import com.green.beadalyo.jhw.security.jwt.JwtTokenProvider;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import com.green.beadalyo.jhw.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@Slf4j
public class SSEApiController
{

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>() ;

    private final Map<Long,SseEmitter> syncUser = new HashMap<>() ;
    private final UserServiceImpl userService;
    private final JwtTokenProvider jwtTokenProvider;

    public SSEApiController(AuthenticationFacade authenticationFacade, UserServiceImpl userServiceImpl, JwtTokenProvider jwtTokenProvider)
    {
        this.userService = userServiceImpl;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getEvent(@RequestParam String token) throws UserNotFoundException
    {

        SseEmitter emitter = new SseEmitter((long) 7200 * 1000);
        emitters.add(emitter);
        User user;
        try {
            MyUserDetails userData = (MyUserDetails) jwtTokenProvider.getUserDetailsFromToken(token) ;
            Long userPk = userData.getMyUser().getUserPk();
            user = userService.getUserEager(userPk);
            syncUser.put(userPk, emitter);
            if (user == null) return null ;
        } catch (Exception e) {
            return null;
        }
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        sendEmitters("연결 완료", user);
        return emitter;
    }


    public void sendEmitters(String str, User user) {

        List<SseEmitter> deadEmitters = new ArrayList<>();
        SseEmitter emt = syncUser.get(user.getUserPk()) ;
        emitters.forEach(emitter -> {

            if (emt != emitter)  return ;
            try {
                emitter.send(SseEmitter.event().data("SSE Event - " + str));
            } catch (IOException e) {
                emitter.completeWithError(e);
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }

}
