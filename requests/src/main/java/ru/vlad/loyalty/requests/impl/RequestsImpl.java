package ru.vlad.loyalty.requests.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.vlad.loyalty.dto.FilmsDTO;
import ru.vlad.loyalty.dto.GenresDTO;
import ru.vlad.loyalty.requests.interfaces.Requests;

/**
 * This class is used for request layer to LoyaltyPlant website
 */
@Component
public class RequestsImpl implements Requests {

    @Value("${application.web.main_url}")
    private String mainUrl;

    @Value("${application.web.genre_list_url}")
    private String genreUrl;

    @Value("${application.web.films_list_url}")
    private String filmsUrl;

    @Value("${application.web.key}")
    private String key;

    private RestTemplate template = new RestTemplate();

    public ResponseEntity<GenresDTO> makeGetRequestForGenres() {
        StringBuilder sb = new StringBuilder();
        return template.getForEntity(sb.append(mainUrl).append(genreUrl).append(key).toString(),
                GenresDTO.class);
    }

    public ResponseEntity<FilmsDTO> makeGetRequestForMovies(Integer page) {
        StringBuilder sb = new StringBuilder();
        return template.getForEntity(sb.append(mainUrl).append(filmsUrl).append(key)
                .append("&page=").append(page).toString(), FilmsDTO.class);
    }
}
