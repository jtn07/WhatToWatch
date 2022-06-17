package com.example.whattowatchbeta.OTT.StreamingModels;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class NewDetails {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String imdbId;
    private String imdbRating;
    private String title;
    private String image;

    public NewDetails(String imdbId, String imdbRating, String title, List<NewDetailsNode> newDetailsNodes,String image) {
        this.imdbId = imdbId;
        this.imdbRating = imdbRating;
        this.title = title;
        this.newDetailsNodes = newDetailsNodes;
        this.image = image;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<NewDetailsNode> newDetailsNodes;

}
