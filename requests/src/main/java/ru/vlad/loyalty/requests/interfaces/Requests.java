package ru.vlad.loyalty.requests.interfaces;

import org.springframework.http.ResponseEntity;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.dto.GenresDTO;

public interface Requests {
    /**
     * @return this method should be used for getting all genres and their id's
     */
    ResponseEntity<GenresDTO> makeGetRequestForGenres();

    /**
     * @param page number of page to be fetched
     * @return a page full of descriptions and metadata for films
     */
    ResponseEntity<FilmsDTO> makeGetRequestForMovies(Integer page);
}
