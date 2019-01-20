package ru.vlad.loyalty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vlad.loyalty.dto.GenreDTO;
import ru.vlad.loyalty.service.interfaces.FilmService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public @ResponseBody List<GenreDTO> getGenres(){
        return service.getGenres();
    }

    @GetMapping("/ratingplain")
    public @ResponseBody ResponseEntity<Double> getAverageRatingByGenre(Integer genreId){
        try {
            return new ResponseEntity<>(service.getAveragePlainRating(genreId), HttpStatus.OK);
        } catch (ExecutionException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/ratingweighted")
    public @ResponseBody ResponseEntity<Double> getWeightedAverageRatingByGenre(Integer genreId){
        try {
            return new ResponseEntity<>(service.getAverageWeightedRating(genreId), HttpStatus.OK);
        } catch (ExecutionException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/stop")
    public @ResponseBody Boolean stopCounting(){
        return service.stopCounting();
    }

    @GetMapping("/check")
    public @ResponseBody String checkStatusPercent(){
        return String.format("%.1f", service.getStatusPercent()) + "%";
    }
}
