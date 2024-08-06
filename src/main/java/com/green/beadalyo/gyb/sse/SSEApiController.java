package com.green.beadalyo.gyb.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SSEApiController
{

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>() ;

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getEvent()
    {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(( ) -> emitters.remove(emitter));
        sendEmitters("연결 완료");
        return emitter;
    }


    public void sendEmitters(String str) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
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
