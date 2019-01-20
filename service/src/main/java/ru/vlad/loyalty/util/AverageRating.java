package ru.vlad.loyalty.util;

import lombok.Data;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;

import java.util.Map;

@Data
class AverageRating {

    private Integer genreId;
    private Requests requests;
    private Map<Integer, Integer> currentResults;

    AverageRating(Integer genreId, Requests requests, Map<Integer, Integer> currentResults) {
        this.genreId = genreId;
        this.requests = requests;
        this.currentResults = currentResults;
    }

    FilmsDTO getFilms(Integer page) {
        currentResults.put(genreId, page);
        return requests.makeGetRequestForMovies(page).getBody();
    }

    void checkForInterrupts() {
        if (Thread.currentThread().isInterrupted()){
            currentResults.put(genreId, 0);
            Thread.currentThread().interrupt();
        }
    }
}
