package com.thehecklers.coffeeservice;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class DataLoader {
    private final CoffeeRepository repo;

    DataLoader(CoffeeRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    private void load() {
        repo.deleteAll().thenMany(
                Flux.just("Kaldi's Coffee", "Espresso Roast", "Blue Bottle", "Philz Coffee", "Tim Hortons", "Peet's",
                        "Pike Place", "Café Bustelo", "Death Wish", "Green Mountain")
                        .map(name -> new Coffee(UUID.randomUUID().toString(), name))
                        .flatMap(repo::save))
                .thenMany(repo.findAll())
                .subscribe(System.out::println);
    }
}
