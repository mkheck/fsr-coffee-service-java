package com.thehecklers.coffeeservice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
//@WebFluxTest({CoffeeController.class, CoffeeService.class})
@WebFluxTest({RouterFunction.class, CoffeeHandler.class, CoffeeService.class})
public class CoffeeServiceExternalAPITests {
    @Autowired
    private WebTestClient client;

    @MockBean
    private CoffeeRepository repo;

    private Coffee coffee1;
    private Coffee coffee2;

    @Before
    public void setup() {
        coffee1 = new Coffee("000-TESTCOFFEE-111", "Tester's Choice");
        coffee2 = new Coffee("000-TESTCOFFEE-222", "Maxfail House");

        Mockito.when(repo.findAll()).thenReturn(Flux.just(coffee1, coffee2));
        Mockito.when(repo.findById(coffee1.getId())).thenReturn(Mono.just(coffee1));
        Mockito.when(repo.findById(coffee2.getId())).thenReturn(Mono.just(coffee2));
    }


    @Test
    public void all() {
        StepVerifier.create(client.get()
                .uri("/coffees")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Coffee.class)
                .getResponseBody())
            .expectNext(coffee1)
            .expectNext(coffee2)
            .verifyComplete();
    }

    @Test
    public void byId() {
        StepVerifier.create(client.get()
                .uri("/coffees/{id}", coffee1.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Coffee.class)
                .getResponseBody())
            .expectNext(coffee1)
            .verifyComplete();
    }

    @Test
    public void orders() {
        StepVerifier.create(client.get()
                .uri("/coffees/{id}/orders", coffee2.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_EVENT_STREAM + ";charset=UTF8")
                .returnResult(CoffeeOrder.class)
                .getResponseBody()
                .take(3))
            .expectNextCount(3)
            .verifyComplete();
    }
}