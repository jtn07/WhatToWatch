package com.example.whattowatchbeta.IMDB.Model.imdbBaseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedSearchResult {
        private String id ;
        private String image ;
        private String title ;
        private String description ;
        private String runtimeStr ;
        private String genres ;
        private List<KeyValueItem> genreList ;
        private String contentRating ;
        private String imDbRating ;
        private String imDbRatingVotes ;
        private String metacriticRating ;
        private String plot ;
        private String stars ;
        private List<StarShort> starList ;


/*"queryString":
        "?title_type=tv_movie,tv_series&release_date=2022-01-01,2022-03-25&genres=action,adventure",
        "results":[{
        "id" "tt2934286",
                "image":"https://imdb-api.com/images/original/MV5BYzhlOTkzZDMtNDYxYS00NTY2LWJjZDEtNjI3NmE3MTI2NGU2XkEyXkFqcGdeQXVyMTM1MTE1NDMx._V1_Ratio0.6837_AL_.jpg",
                        "title":"Halo",
                        "description":"(2022� )",
                        "runtimeStr":"60 min",
                        "genres":"Action, Adventure, Sci-Fi",
                        "genreList":[{"key":"Action","value":"Action"},{"key":"Adventure","value":"Adventure"},{"key":"Sci-Fi","value":"Sci-Fi"}],
                "contentRating":"TV-14",
                        "imDbRating":"7.0",
                        "imDbRatingVotes":"41292",
                        "metacriticRating":null,
                        "plot":"Aliens threaten human existence in an epic 26th-century showdown. TV series based on the video game 'Halo.'",
                        "stars":"Pablo Schreiber, Shabana Azmi, Natasha Culzac, Olive Gray",
                        "starList":[{"id":"tt2934286","name":"Pablo Schreiber"}*/
}
