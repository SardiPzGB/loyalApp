package ru.vlad.loyalty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This DTO describes a genre of a movie. It contains id and name of a genre, what could be said more?
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreDTO{
    private Integer id;
    private String name;
}
