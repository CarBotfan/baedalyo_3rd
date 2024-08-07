package com.green.beadalyo.gyb.sse;

import com.green.beadalyo.jhw.security.AuthenticationFacade;
import com.green.beadalyo.jhw.user.UserServiceImpl;
import com.green.beadalyo.jhw.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SSEApiController
{

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>() ;

    private final Map<User,SseEmitter> syncUser = new HashMap<>() ;
    private final AuthenticationFacade authenticationFacade;
    private final UserServiceImpl userService;

    public SSEApiController(AuthenticationFacade authenticationFacade, UserServiceImpl userServiceImpl)
    {
        this.authenticationFacade = authenticationFacade;
        this.userService = userServiceImpl;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getEvent()
    {

        SseEmitter emitter = new SseEmitter((long)7200*1000);
        emitters.add(emitter);

        Long userPk = authenticationFacade.getLoginUserPk() ;
        User user = userService.getUser(userPk) ;

        syncUser.put(user,emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(( ) -> emitters.remove(emitter));
        sendEmitters("연결 완료", user);
        return emitter;
    }


    public void sendEmitters(String str, User user) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            SseEmitter emt = syncUser.get(user) ;
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
