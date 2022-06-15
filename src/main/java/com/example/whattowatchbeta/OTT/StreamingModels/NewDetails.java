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

    public NewDetails(String imdbId, List<NewDetailsNode> newDetailsNodes) {
        this.imdbId = imdbId;
        this.newDetailsNodes = newDetailsNodes;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<NewDetailsNode> newDetailsNodes;
}
