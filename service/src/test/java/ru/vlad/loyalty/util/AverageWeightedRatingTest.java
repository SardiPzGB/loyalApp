package ru.vlad.loyalty.util;

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
public class AverageWeightedRatingTest {

    @Mock
    private Requests mockRequests;

    private AverageWeightedRating rating;
    private ResponseEntity<FilmsDTO> responseEntity;
    private FilmDTO[] films = new FilmDTO[3];
    private Integer[] voteCounts = new Integer[]{150, 60, 20};
    private Double[] averageVotes = new Double[] {8.0, 8.0, 8.0};

    @Before
    public void setUp() {
        Map<Integer, Integer> currentResults = new HashMap<>();
        rating = new AverageWeightedRating(1, 2, mockRequests, currentResults);
        Integer[] ids = new Integer[]{1, 2};
        Integer[] date = new Integer[] {2000, 1, 1};
        films[0] = new FilmDTO(1, voteCounts[0], averageVotes[0], "title1", 5.0,
                "en", "title1", ids, false, "good",  date);
        films[1] = new FilmDTO(2, voteCounts[1], averageVotes[1], "title1", 5.0,
                "en", "title1", ids, false, "medium",  date);
        films[2] = new FilmDTO(3, voteCounts[2], averageVotes[2], "title1", 5.0,
                "en", "title1", ids, false, "bad",  date);
        FilmsDTO filmsDTO = new FilmsDTO(1, 1, 2, films);
        responseEntity = new ResponseEntity<>(filmsDTO, OK);
    }

    @Test
    public void whenCalc_thenCorrect() {
        //given
        when(mockRequests.makeGetRequestForMovies(1)).thenReturn(responseEntity);

        //when
        Double result = rating.call();

        //then
        assert ((voteCounts[0]*averageVotes[0] +
                voteCounts[1]*averageVotes[1] +
                voteCounts[2]*averageVotes[2])/(voteCounts[0]+voteCounts[1]+voteCounts[2]) == result);
    }

}