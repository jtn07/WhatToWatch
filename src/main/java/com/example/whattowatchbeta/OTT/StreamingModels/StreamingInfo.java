package com.example.whattowatchbeta.OTT.StreamingModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class StreamingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String imdbId;

    public StreamingInfo(String imdbId, List<InNode> in) {
        this.imdbId = imdbId;
        In = in;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JsonProperty("in")
    List<InNode> In;

}
