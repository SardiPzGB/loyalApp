package ru.vlad.loyalty.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import ru.vlad.loyalty.dto.FilmDTO;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class AverageRatingTest {

    @Mock
    private Requests mockRequests;

    private AverageRating rating;
    private Map<Integer, Integer> currentResults = new HashMap<>();
    private ResponseEntity<FilmsDTO> responseEntity;
    private FilmDTO[] films = new FilmDTO[2];

    @Before
    public void setUp() {
        films[0] = new FilmDTO();
        films[1] = new FilmDTO();
        FilmsDTO filmsDTO = new FilmsDTO(1, 1, 2, films);
        rating = new AverageRating(1, mockRequests, currentResults);
        responseEntity = new ResponseEntity<>(filmsDTO, OK);
    }

    @Test
    public void whenGetFilms_thenReturnFilmsDTO(){
        //given
        when(mockRequests.makeGetRequestForMovies(1)).thenReturn(responseEntity);

        //when
        FilmsDTO result = rating.getFilms(1);

        //then
        Assert.assertArrayEquals(films, result.getResults());
    }
}