package ru.vlad.loyalty.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.dto.GenreDTO;
import ru.vlad.loyalty.dto.GenresDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;
import ru.vlad.loyalty.service.interfaces.FilmService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class FilmServiceImplTest {

    @Mock
    private Requests requests;

    private FilmService service;

    private GenresDTO mockGenres = mock(GenresDTO.class);
    private ResponseEntity<GenresDTO> mockResponseGenres = new ResponseEntity<>(mockGenres, OK);
    private GenreDTO[] arrayGenres = new GenreDTO[2];

    @Before
    public void setUp()  {
        arrayGenres[0] = new GenreDTO(1, "genre1");
        arrayGenres[1] = new GenreDTO(2, "genre2");
        when(mockGenres.getGenres()).thenReturn(arrayGenres);
        service = new FilmServiceImpl(requests);
    }

    @Test
    public void whenGetGenres_thenReceiveListGenreDTO(){
        //given
        when(requests.makeGetRequestForGenres()).thenReturn(mockResponseGenres);

        //when
        List<GenreDTO> result = service.getGenres();

        //then
        assertArrayEquals(arrayGenres, result.toArray());
    }

    @Test
    public void whenGetStatusWhenNoPages_thenZero(){
        //given
        FilmsDTO dto = new FilmsDTO();
        dto.setTotalPages(0);

        when(requests.makeGetRequestForMovies(1))
                .thenReturn(new ResponseEntity<>(dto, OK));

        //when
        Double result = service.getStatusPercent();

        //then
        assert(0.0 == result);
    }

    @Test
    public void whenCalcWeightedNoGenre_thenGetZero() throws ExecutionException {
        //given

        //when
        Double result = service.getAverageWeightedRating(null);

        //then
        assert(0.0 == result);
    }

    @Test
    public void whenCalcPlainNoGenre_thenGetZero() throws ExecutionException {
        //given

        //when
        Double result = service.getAveragePlainRating(null);

        //then
        assert(0.0 == result);
    }

    @Test
    public void whenStoppedWhileNotCounting_thenFalse(){
        //given

        //when
        Boolean result = service.stopCounting();

        //then
        assertFalse(result);
    }
}