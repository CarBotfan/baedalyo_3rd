package com.green.beadalyo.gyb.crollring.geocoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.beadalyo.gyb.model.Restaurant;
import com.green.beadalyo.gyb.restaurant.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Hidden
@RestController
@RequiredArgsConstructor
public class OpenDataWebClientService {
    private final WebClient webClient = WebClient.builder().baseUrl("https://api.vworld.kr/").build();
    private final RestaurantRepository repository;

    @GetMapping("test/geo")
    public void getOpenData() {
        int page = 0;
        int size = 300; // 한번에 가져올 데이터의 수
        Page<Restaurant> geoCoderPage;

        do {
            // 페이지 요청 생성
            PageRequest pageRequest = PageRequest.of(page, size);
            geoCoderPage = repository.findByCoorXIsNull(pageRequest);

            // 데이터를 처리하는 로직
            Flux.fromIterable(geoCoderPage)
                    .delayElements(Duration.ofMillis(100))
                    .flatMap(i -> webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("req/address")
                                    .queryParam("service", "address")
                                    .queryParam("request", "getcoord")
                                    .queryParam("version", "2.0")
                                    .queryParam("crs", "EPSG:4326")
                                    .queryParam("address", i.getAddress())
                                    .queryParam("refine", "false")
                                    .queryParam("simple", "true")
                                    .queryParam("format", "json")
                                    .queryParam("type", "PARCEL")
                                    .queryParam("key", "C0C31729-5079-319F-A763-30A9E5957834")
                                    .build())
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                            .flatMap(info -> {
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    String elem = objectMapper.writeValueAsString(info);
                                    JsonNode rootNode = objectMapper.readTree(elem);
                                    JsonNode itemsNodeX = rootNode.path("response").path("result").path("point").path("x");
                                    JsonNode itemsNodeY = rootNode.path("response").path("result").path("point").path("y");
                                    String x = itemsNodeX.asText();
                                    String y = itemsNodeY.asText();
                                    System.out.println(i.getAddress());
                                    if (!x.isEmpty() && !y.isEmpty()) {
                                        i.setCoorX(new BigDecimal(x));
                                        i.setCoorY(new BigDecimal(y));
                                        System.out.println(x + " " + y);
                                    } else {
                                        return Mono.empty();
                                    }
                                    return Mono.just(i);
                                } catch (Exception e) {
                                    return Mono.error(e);
                                }
                            })
                            .doOnError(e -> {
                                System.err.println("Error during API call or processing: " + e.getMessage());
                            })
                    )
                    .collectList() // Flux<GeoCoder>를 List<GeoCoder>로 변환
                    .flatMapMany(updatedData -> {
                        try {
                            List<Restaurant> savedData = repository.saveAll(updatedData);
                            return Flux.fromIterable(savedData);
                        } catch (Exception e) {
                            System.err.println("Error during saving to the repository: " + e.getMessage());
                            return Flux.error(e);
                        }
                    })
                    .doOnError(e -> {
                        System.err.println("Error during saving or processing: " + e.getMessage());

                    })
                    .subscribe(
                            saved -> System.out.println("Data saved: " + saved),
                            error -> {
                                System.err.println("Error during subscription: " + error.getMessage());
                            }
                    );

            page++;
        } while (geoCoderPage.hasNext());
//        } while (false);
    }

    public void addressFilter()
    {
        List<Restaurant> coder = repository.findByCoorXIsNull();
        coder.forEach(geoCoder -> {
            String[] split = geoCoder.getAddress().split(" ");
            if(split.length < 4) return  ;
            geoCoder.setAddress(split[0]+" "+split[1]+" "+split[2]+" "+split[3]);
        });
        repository.saveAll(coder) ;

    }

}
