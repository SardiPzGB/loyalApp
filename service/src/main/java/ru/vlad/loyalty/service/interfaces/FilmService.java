package ru.vlad.loyalty.service.interfaces;

import ru.vlad.loyalty.dto.GenreDTO;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FilmService {
    /**
     * This method should be used for getting list of movie genres for statistics calculation
     * @return list of genres and their ID's
     */
    List<GenreDTO> getGenres();

    /**
     * @param genreId id of genre for calculation its average rating
     * @return average rating of a genre
     */
    Double getAveragePlainRating(Integer genreId) throws ExecutionException;


    /**
     * @param genreId id of genre for calculation its average rating
     * @return average weighted rating of a genre
     */
    Double getAverageWeightedRating(Integer genreId) throws ExecutionException;

    /**
     * Should be used for stopping calculation
     */
    Boolean stopCounting();

    Double getStatusPercent();
}
