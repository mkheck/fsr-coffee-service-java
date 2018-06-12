package com.thehecklers.coffeeservice;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CoffeeRepository extends ReactiveCrudRepository<Coffee, String> {

}

