package com.example.whattowatchbeta.IMDB.Model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "IMDBMOVIEENTITY")
public class IMDBMovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long genId;
    @NonNull
    @Column(unique = true)
    private String id ;
    private String image ;
    private String title ;
    private String description ;
    private String runtimeStr ;
    private String genres ;
    private String iMDbRating ;
    private String iMDbRatingVotes ;
    private Date receivedDate;
    private boolean ottAvailable;
    private Date lastChecked;
    public IMDBMovieEntity(String id) {
        this.id=id;
    }
}
