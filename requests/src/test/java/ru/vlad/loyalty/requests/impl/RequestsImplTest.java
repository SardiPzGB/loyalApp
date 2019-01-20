package ru.vlad.loyalty.requests.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.dto.GenresDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestsImplTest {

    private GenresDTO mockGenres = mock(GenresDTO.class);
    private FilmsDTO mockFilms = mock(FilmsDTO.class);

    @Mock
    private RestTemplate mockTemplate;

    @InjectMocks
    Requests requests = new RequestsImpl();

    @Before
    public void setup(){
    }

    @Test
    public void whenRequestingGenres_thenReceiveListGenres() {
        //given
        when(mockTemplate.getForEntity(anyString(), eq(GenresDTO.class)))
                .thenReturn(new ResponseEntity<>(mockGenres, HttpStatus.OK));

        //when
        ResponseEntity<GenresDTO> res = requests.makeGetRequestForGenres();

        //then
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), mockGenres);
    }

    @Test
    public void whenRequestingFilms_thenReceiveFilmsDTO() {
        //given
        when(mockTemplate.getForEntity(anyString(), eq(FilmsDTO.class)))
                .thenReturn(new ResponseEntity<>(mockFilms, HttpStatus.OK));

        //when
        ResponseEntity<FilmsDTO> res = requests.makeGetRequestForMovies(1);

        //then
        assertEquals(res.getStatusCode(), HttpStatus.OK);
        assertEquals(res.getBody(), mockFilms);
    }
}