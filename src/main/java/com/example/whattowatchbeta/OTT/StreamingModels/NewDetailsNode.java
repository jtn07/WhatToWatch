package com.example.whattowatchbeta.OTT.StreamingModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class NewDetailsNode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String link;

    public NewDetailsNode(String link) {
        this.link = link;
    }
}
