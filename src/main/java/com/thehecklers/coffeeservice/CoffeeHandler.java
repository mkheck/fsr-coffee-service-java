package com.thehecklers.coffeeservice;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CoffeeHandler {
    private final CoffeeService service;

    CoffeeHandler(CoffeeService service) {
        this.service = service;
    }

    public Mono<ServerResponse> all(ServerRequest req) {
        return ServerResponse.ok()
                .body(service.getAllCoffees(), Coffee.class);
    }

    public Mono<ServerResponse> byId(ServerRequest req) {
        return ServerResponse.ok()
                .body(service.getCoffeeById(req.pathVariable("id")), Coffee.class);
    }

    public Mono<ServerResponse> orders(ServerRequest req) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(service.getOrders(req.pathVariable("id")), CoffeeOrder.class);
    }
}
