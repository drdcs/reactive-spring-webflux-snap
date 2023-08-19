package com.learnreactiveprogramming.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public static final Logger log = LoggerFactory.getLogger(FluxAndMonoGeneratorService.class);


    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("dip", "sma", "dini", "mama")).log();
    }


    public Flux<String> namesFlux_immutability() {
        Flux<String> stringFlux = Flux.fromIterable(List.of("dip", "sma", "dini", "mama"));
        stringFlux.map(String::toUpperCase);
        return stringFlux;
    }

    public Flux<String> namesFlux_flatmap(int stringlen) {
        return
                Flux.fromIterable(List.of("dip", "sma", "dini", "mama"))
                        .map(String::toUpperCase)
                        .filter(s -> s.length() > stringlen)
                        .flatMap(this::splitString)
                        .log()
                ;
    }

    public Flux<String> namesFlux_flatmap_async(int stringlen) {
        return
                Flux.fromIterable(List.of("dip", "sma", "dini", "mama"))
                        .map(String::toUpperCase)
                        .filter(s -> s.length() > stringlen)
                        .flatMap(this::splitString_delay)
                        .log();
    }

    public Flux<String> namesFlux_concatMap_async(int stringlen) {
        return
                Flux.fromIterable(List.of("dip", "sma", "dini", "mama"))
                        .map(String::toUpperCase)
                        .filter(s -> s.length() > stringlen)
                        .concatMap(this::splitString_delay)
                        .log();
    }



    public Flux<String> namesFlux_transform(int stringLen) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLen);

        return
                Flux.fromIterable(List.of("dip", "sma", "dini", "mama"))
                        .transform(filterMap)
                        .flatMap(this::splitString)
                        .defaultIfEmpty("default")
                        .log();
    }

    public Flux<String> namesFlux_switchifempty(int stringLen) {



        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLen)
                        .flatMap(s -> splitString(s));

        Flux<String> aDefault = Flux.just("default")
                .transform(filterMap);
        return
                Flux.fromIterable(List.of("dip", "sma", "dini", "mama"))
                        .transform(filterMap)
                        .flatMap(this::splitString)
                        .switchIfEmpty(aDefault)
                        .log();
    }



    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitString_delay(String name) {
        var charArray = name.split("");
        int delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }


    static String process(String value) {
        return value.toUpperCase();
    }

    static void timeTaken(String value) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    // mono
    public Mono<List<String>> namesMono_flatMap(int stringlen) {
        return
                Mono.just("dini")
                        .map(String::toUpperCase)
                        .filter(s -> s.length() > stringlen)
                        .flatMap(this::splitStringMono)
                        ;
    }

    public Flux<String> namesMono_flatMapMany(int stringlen) {
        return
                Mono.just("dini")
                        .map(String::toUpperCase)
                        .filter(s -> s.length() > stringlen)
                        .flatMapMany(this::splitString);
    }

    private Mono<List<String>> splitStringMono(String s) {

        var charArray = s.split("");
        List<String> charList = List.of(charArray);
        return Mono.just(charList);
    }



    public Flux<String> explore_concat() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux);
    }

    public Flux<String> explore_concat_with() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E", "F");
        return abcFlux.concatWith(defFlux);
    }

    public Flux<String> explore_concat_mono() {
        var abcFlux = Mono.just("A");
        Flux<String> defFlux = Flux.just("D", "E", "F");
        return abcFlux.concatWith(defFlux);
    }


    public Flux<String> explore_merge() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> explore_merge_seq() {
        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abcFlux, defFlux).log();
    }


    public Flux<String> explore_zip() {
        Flux<String> abcFlux = Flux.just("A", "B", "C");
        Flux<String> defFlux = Flux.just("D", "E");
        return Flux.zip(abcFlux, defFlux, (a, b) -> a + b).log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService na  = new FluxAndMonoGeneratorService();
        na.namesFlux()
                .doOnNext(r -> log.info("names is {}", r))
                .doOnNext(FluxAndMonoGeneratorService::timeTaken)
                .map(FluxAndMonoGeneratorService::process)
                .filter(name -> name.startsWith("M"))
                .doOnNext(r -> log.info("names is {}", r))
                .subscribe();
    }






}
