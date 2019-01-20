package ru.vlad.loyalty.util;

import lombok.EqualsAndHashCode;
import ru.vlad.loyalty.dto.FilmDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;

import java.util.Map;
import java.util.concurrent.Callable;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static ru.vlad.loyalty.util.AverageCollector.averagingWeighted;

@EqualsAndHashCode(callSuper = true)
public class AverageWeightedRating extends AverageRating implements Callable<Double> {

    private Integer numOfPages;

    public AverageWeightedRating(Integer genreId, Integer numOfPages, Requests requests, Map<Integer, Integer> currentResults) {
        super(genreId, requests, currentResults);
        this.numOfPages = numOfPages;
    }

    @Override
    public Double call(){
        Box averageWeightedRating = new Box();
        for (int i = 1; i < numOfPages; i++){
            checkForInterrupts();
            averageWeightedRating.combine( stream(getFilms(i).getResults())
                    .filter(filmDTO -> asList(filmDTO.getGenreIds())
                            .contains(getGenreId()))
                    .collect(averagingWeighted(FilmDTO::getVoteAverage, FilmDTO::getVoteCount))
            );
        }
        return averageWeightedRating.getNum()/averageWeightedRating.getDen();
    }
}
