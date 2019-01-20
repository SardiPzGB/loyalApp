package ru.vlad.loyalty.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.dto.GenreDTO;
import ru.vlad.loyalty.dto.GenresDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;
import ru.vlad.loyalty.service.interfaces.FilmService;
import ru.vlad.loyalty.util.AveragePlainRating;
import ru.vlad.loyalty.util.AverageWeightedRating;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.Arrays.asList;

@Service
public class FilmServiceImpl implements FilmService {

    private Requests requests;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Map<Integer, Future<Double>> launchedCalculations = new HashMap<>();
    private volatile Map<Integer, Integer> currentResults = new HashMap<>();
    private Integer currentGenre;

    @Autowired
    public FilmServiceImpl(Requests requests) {
        this.requests = requests;
    }

    @Override
    public List<GenreDTO> getGenres() {
        ResponseEntity<GenresDTO> response = requests.makeGetRequestForGenres();
        if (response.getStatusCode() != HttpStatus.OK){
            return Collections.emptyList();
        }
        return asList(response.getBody().getGenres());
    }

    @Override
    public Double getAverageWeightedRating(Integer genreId) throws ExecutionException {
        if (genreId == null){
            return 0.0;
        }
        Callable<Double> callable = new AverageWeightedRating(genreId, getNumOfPages(), requests, currentResults);
        return launchAndGetResult(callable, genreId);
    }

    @Override
    public Double getAveragePlainRating(Integer genreId) throws ExecutionException {
        if (genreId == null){
            return 0.0;
        }
        Callable<Double> callable = new AveragePlainRating(genreId, getNumOfPages(), requests, currentResults);
        return launchAndGetResult(callable, genreId);
    }

    @Override
    public Boolean stopCounting() {
        final Boolean[] result = new Boolean[1];    // we can have only one background calculation task
        launchedCalculations.forEach((integer, doubleFuture) -> result[0] = doubleFuture.cancel(true));
        return result[0]==null ? Boolean.FALSE : result[0];
    }

    @Override
    public Double getStatusPercent() {
        int numOfPages = getNumOfPages() - 1;
        if (numOfPages<=0 || currentGenre==null) {
            return 0.0;
        }

        return (double) currentResults.get(currentGenre) * 100 / numOfPages;
    }

    //------------------------------ private methods ---------------------

    private int getNumOfPages() {
        // any page starting from 1 contains total number of pages
        ResponseEntity<FilmsDTO> response = requests.makeGetRequestForMovies(1);
        if (response.getStatusCode() != HttpStatus.OK){
            return 0;
        }
        return response.getBody().getTotalPages();
    }

    private Double launchAndGetResult(Callable<Double> callable, Integer genreId)
            throws ExecutionException {
        currentGenre = genreId;
        Future<Double> future = executorService.submit(callable);
        launchedCalculations.put(genreId, future);
        Double result = 0.0;
        try {
            result = future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            launchedCalculations.remove(genreId);
        }
        return result;
    }
}
