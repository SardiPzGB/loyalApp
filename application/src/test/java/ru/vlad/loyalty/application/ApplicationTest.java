package ru.vlad.loyalty.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.vlad.loyalty.config.ApplicationConfig;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ApplicationConfig.class })
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.yml")
@EnableWebMvc
@Slf4j
public class ApplicationTest {

    private String GENRE_ID_PARAM = "genreId";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void whenServletContext_thenProvidesController() {
        ServletContext servletContext = wac.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(wac.getBean("filmController"));
    }

    @Test
    public void whenGetGenres_thenReceiveGenres() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(get("/films/genres")
                .contentType(APPLICATION_JSON));

        //then
        result.andDo(print())
                .andExpect(content().json("[{\"id\":10752,\"name\":\"War\"},{\"id\":10402,\"name\":\"Music\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":36,\"name\":\"History\"},{\"id\":37,\"name\":\"Western\"},{\"id\":12,\"name\":\"Adventure\"},{\"id\":878,\"name\":\"Science Fiction\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":10770,\"name\":\"TV Movie\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":28,\"name\":\"Action\"},{\"id\":10749,\"name\":\"Romance\"},{\"id\":10751,\"name\":\"Family\"}]"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCalculateAveragePlainFor99AndDoChecks_thenReceiveResult() throws Exception {
        //given

        //when
        new Thread(() -> {
            try {
                MvcResult result = mockMvc.perform(get("/films/ratingplain")
                        .param(GENRE_ID_PARAM, "99")
                        .contentType(APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                .andReturn();
                log.info("Final plain average for documental is {}",
                        result.getResponse().getContentAsString());
            } catch (Exception e) {
                log.error("Cannot calculate rating", e);
            }
        }).start();


        //then
        String status = "";

        while(!status.equals("100,0%")){
            Thread.sleep(5000);

            // check status
            ResultActions res = mockMvc.perform(get("/films/check")
                    .param(GENRE_ID_PARAM, "99")
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
            status = res.andReturn().getResponse().getContentAsString();
            log.info("Current status: {}", status);
        }
    }

    @Test
    public void whenCalculateAverageWeightedFor99AndDoChecks_thenReceiveResult() throws Exception {
        //given

        //when
        new Thread(() -> {
            try {
                MvcResult res = mockMvc.perform(get("/films/ratingweighted")
                        .param(GENRE_ID_PARAM, "99")
                        .contentType(APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                .andReturn();
                log.info("Final weighted average for documental is {}",
                        res.getResponse().getContentAsString());
            } catch (Exception e) {
                log.error("Cannot calculate rating", e);
            }
        }).start();

        //then
        String status = "";

        while(!status.equals("100,0%")){
            Thread.sleep(5000);

            // check status
            ResultActions res = mockMvc.perform(get("/films/check")
                    .param(GENRE_ID_PARAM, "99")
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
            status = res.andReturn().getResponse().getContentAsString();
            log.info("Current status: {}", status);
        }
    }

    @Test
    public void whenCalculatingAveragePlain_canStopCalculations() throws Exception {
        //given

        //when
        new Thread(() -> {
            try {
                mockMvc.perform(get("/films/ratingplain")
                        .param(GENRE_ID_PARAM, "99")
                        .contentType(APPLICATION_JSON))
                        .andDo(print())
                        .andReturn();
            } catch (Exception e) {
                log.error("Calculation was stopped", e);
            }
        }).start();

        Thread.sleep(10000);
        mockMvc.perform(get("/films/stop")
        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void whenCalculatingAverageWeighted_canStopCalculations() throws Exception {
        //given

        //when
        new Thread(() -> {
            try {
                mockMvc.perform(get("/films/ratingweighted")
                        .param(GENRE_ID_PARAM, "99")
                        .contentType(APPLICATION_JSON))
                        .andDo(print())
                        .andReturn();
            } catch (Exception e) {
                log.error("Calculation was stopped", e);
            }
        }).start();

        Thread.sleep(10000);
        mockMvc.perform(get("/films/stop")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}