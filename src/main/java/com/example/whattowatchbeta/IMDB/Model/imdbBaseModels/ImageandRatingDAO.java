package com.example.whattowatchbeta.IMDB.Model.imdbBaseModels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageandRatingDAO {
    private Long iMDbRating;
    private String title;
    private String image;
}
