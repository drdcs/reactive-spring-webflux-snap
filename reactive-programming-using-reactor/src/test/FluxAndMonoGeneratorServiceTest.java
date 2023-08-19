package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FluxAndMonoGeneratorServiceTest {
    
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = 
            new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {

        Flux<String> names = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier.create(names)
                .expectNext("dip", "sma", "dini", "mama")
                .verifyComplete();
    }

    @Test
    void namesFlux_1() {

        Flux<String> names = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier.create(names)
                .expectNext("dip")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFlux_immutibilty_test() {

        Flux<String> names = fluxAndMonoGeneratorService.namesFlux_immutability();
        StepVerifier.create(names)
                .expectNext("DIP")
                .expectNextCount(3)
                .verifyComplete();
    }


    @Test
    void namesFlux_flatmap_test() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFlux_flatmap(3);
        StepVerifier.create(names)
                .expectNext("D","I","N","I", "M","A","M","A")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap_async_test() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFlux_flatmap_async(3);
        StepVerifier.create(names)
//                .expectNext("D","I","N","I", "M","A","M","A")
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatmap_async_test() {
        Flux<String> names = fluxAndMonoGeneratorService.namesFlux_concatMap_async(3);
        StepVerifier.create(names)
                .expectNext("D","I","N","I", "M","A","M","A")
//                .expectNextCount(8)

                .verifyComplete();
    }

    // Mono

    @Test
    void namesMono_flatMap_test() {

        Mono<List<String>> listMono = fluxAndMonoGeneratorService.namesMono_flatMap(3);

        StepVerifier.create(listMono)
                .expectNext(List.of("D", "I", "N", "I"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany_test() {

        Flux<String> listMono = fluxAndMonoGeneratorService.namesMono_flatMapMany(3);

        StepVerifier.create(listMono)
                .expectNext("D", "I", "N", "I")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_test(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_transform(6);
        StepVerifier.create(stringFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_switchifempty_test(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_switchifempty(6);
        StepVerifier.create(stringFlux)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void explore_concat_mono_test(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.explore_concat_mono();
        StepVerifier.create(stringFlux)
                .expectNext("A", "D", "E", "F")
                .verifyComplete();
    }
    
    @Test
    void explore_merge_test(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.explore_merge();
        StepVerifier.create(stringFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_merge_seq_test(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.explore_merge_seq();
        StepVerifier.create(stringFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void explore_zip_test(){
        Flux<String> stringFlux = fluxAndMonoGeneratorService.explore_zip();
        StepVerifier.create(stringFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}
