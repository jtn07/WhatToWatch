package com.example.whattowatchbeta.IMDB.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long jobId;
    private Date date;

    public JobDetails(Date date){
        new Date();
    }

}
