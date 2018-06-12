package com.thehecklers.coffeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class CoffeeServiceApplication {
    @Bean
    RouterFunction<ServerResponse> routerFunction(CoffeeHandler handler) {
        return route(GET("/coffees"), handler::all)
                .andRoute(GET("/coffees/{id}"), handler::byId)
                .andRoute(GET("/coffees/{id}/orders"), handler::orders);
    }

    public static void main(String[] args) {
        SpringApplication.run(CoffeeServiceApplication.class, args);
    }
}
