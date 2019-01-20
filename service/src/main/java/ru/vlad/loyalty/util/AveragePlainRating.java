package ru.vlad.loyalty.util;

import lombok.EqualsAndHashCode;
import ru.vlad.loyalty.dto.FilmDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;

import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.summarizingDouble;

@EqualsAndHashCode(callSuper = true)
public class AveragePlainRating extends AverageRating implements Callable<Double>  {

    private int numOfPages;

    public AveragePlainRating(Integer genreId, int numOfPages, Requests requests, Map<Integer, Integer> currentResults) {
        super(genreId, requests, currentResults);
        this.numOfPages = numOfPages;
    }

    @Override
    public Double call() {

        DoubleSummaryStatistics dss = new DoubleSummaryStatistics();
        for (int i = 1; i < numOfPages; i++){
            checkForInterrupts();
            dss.combine(stream(getFilms(i).getResults())
                    .filter(filmDTO -> asList(filmDTO.getGenreIds())
                            .contains(getGenreId()))
                    .collect(summarizingDouble(FilmDTO::getVoteAverage))
            );
        }
        return dss.getAverage();
    }
}
