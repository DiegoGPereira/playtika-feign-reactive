package com.dev.diegopereira.service;

import com.dev.diegopereira.config.MovieClient;
import com.dev.diegopereira.response.MovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieService implements CommandLineRunner {

    @Autowired
    private MovieClient movieClient;

    @Override
    public void run(String... args) throws Exception {

        Flux.concat(
                getAll(), // first get all movies
                post(),   // create a new movie id 5
                get(),    // get movie id 5 - check if it is present
                put(),    // update movie id 5
                get(),    // get movie id 5 - check if it is updated
                delete()  // delete movie id 5
        ).subscribe();

    }

    private Mono<Void> getAll(){
        return this.movieClient.getAllMovies()
                .doOnNext(System.out::println)
                .doFinally(s -> System.out.println("------------- GET All completed ------------------"))
                .then();
    }

    private Mono<Void> post(){
        MovieDto dto = MovieDto.create(
                5,
                "Harry Potter and the Sorcerer Stone",
                1999,
                7.6
        );
        return this.movieClient.saveMovie(dto)
                .doFinally(s -> System.out.println("------------- POST Movie completed ------------------"))
                .then();
    }

    private Mono<Void> put(){
        MovieDto dto = MovieDto.create(
                null,
                "Harry Potter and the Sorcerer Stone",
                2001,
                7.6
        );
        return this.movieClient.updateMovie(5, dto)
                .doOnNext(System.out::println)
                .doFinally(s -> System.out.println("------------- Movie updated ------------------"))
                .then();
    }

    private Mono<Void> get(){
        return this.movieClient.getMovie(5)
                .doOnNext(System.out::println)
                .doFinally(s -> System.out.println("------------- GET Movie completed ------------------"))
                .then();
    }

    private Mono<Void> delete(){
        return this.movieClient.deleteMovie(5)
                .doOnNext(System.out::println)
                .doFinally(s -> System.out.println("------------- Movie deleted ------------------"))
                .then();
    }
}