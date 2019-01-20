package ru.vlad.loyalty.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.vlad.loyalty.dto.GenreDTO;
import ru.vlad.loyalty.service.interfaces.FilmService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class FilmControllerTest {

    @Mock
    private FilmService service;

    private List<GenreDTO> mockGenreList = new ArrayList<>();
    private MockMvc mockMvc;

    @Before
    public void setup(){

        mockGenreList.add(new GenreDTO(1, "genre1"));
        mockGenreList.add(new GenreDTO(2, "genre2"));
        when(service.getGenres()).thenReturn(mockGenreList);

        FilmController controller = new FilmController(service);
        this.mockMvc = standaloneSetup(controller).build();
    }

    @Test
    public void whenGetGenres_thenGetList() throws Exception {

        // given

        // when
        ResultActions result = mockMvc.perform(get("/films/genres")
            .contentType(APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"genre1\"},{\"id\":2,\"name\":\"genre2\"}]"));
    }

    @Test
    public void whenStopCountingWithoutCalc_thenFalse() throws Exception {
        // given
        when(service.stopCounting()).thenReturn(false);

        //when
        ResultActions result = mockMvc.perform(get("/films/stop")
                .contentType(APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void whenCheckStatusWithoutCalc_thenZero() throws Exception {
        // given
        when(service.getStatusPercent()).thenReturn(0.0);

        //when
        ResultActions result = mockMvc.perform(get("/films/check")
                .contentType(APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("0,0%"));
    }

    @Test
    public void whenStartCalcPlainWithNoGenre_thenZero() throws Exception {
        //given
        when(service.getAveragePlainRating(null)).thenReturn(0.0);

        //when
        ResultActions result = mockMvc.perform(get("/films/ratingplain")
                .contentType(APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    public void whenStartCalcWeightedWithNoGenre_thenZero() throws Exception {
        //given
        when(service.getAveragePlainRating(null)).thenReturn(0.0);

        //when
        ResultActions result = mockMvc.perform(get("/films/ratingweighted")
                .contentType(APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("0.0"));
    }

    @Test
    public void whenStartCalcPlainWithId_thenGetResult() throws Exception {
        // given
        when(service.getAveragePlainRating(1)).thenReturn(100.0);

        //when
        ResultActions result = mockMvc.perform(get("/films/ratingplain")
                .contentType(APPLICATION_JSON)
                .param("genreId", String.valueOf(1)));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }

    @Test
    public void whenStartCalcWeightedWithId_thenGetResult() throws Exception {
        // given
        when(service.getAverageWeightedRating(1)).thenReturn(100.0);

        //when
        ResultActions result = mockMvc.perform(get("/films/ratingweighted")
                .contentType(APPLICATION_JSON)
                .param("genreId", String.valueOf(1)));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }
}