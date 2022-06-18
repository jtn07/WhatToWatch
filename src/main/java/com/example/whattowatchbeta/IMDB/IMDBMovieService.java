package com.example.whattowatchbeta.IMDB;


import com.example.whattowatchbeta.IMDB.Model.IMDBMovieEntity;
import com.example.whattowatchbeta.IMDB.Model.JobDetails;
import com.example.whattowatchbeta.IMDB.Model.imdbBaseModels.AdvancedSearchData;
import com.example.whattowatchbeta.IMDB.Model.imdbBaseModels.AdvancedSearchResult;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import org.slf4j.Logger;

@Service
@NoArgsConstructor
@EnableScheduling
public class IMDBMovieService {

    @Autowired
    private IMDBMovieEntityRepository imdbMovieEntityRepository;
    private Logger logger= LoggerFactory.getLogger(IMDBMovieService.class);
    @Autowired
    private JobDetailsRepository jobDetailsRepository;
    @Autowired
    private IMDBConfig imdbConfig;

    private RestTemplate restTemplate= new RestTemplate();

    @Scheduled(cron = "0 0 18 * * FRI")
    public void scheduledAddNewService() throws ParseException {
        Optional<JobDetails> jobDetails = jobDetailsRepository.getByJobId(1L);
        if (!jobDetails.isPresent()) {
            jobDetailsRepository.save(new JobDetails(1L, new Date()));
        }
        Optional<JobDetails> jobDetails1 = jobDetailsRepository.getByJobId(1L);
        String lastJobDateString = new GetDate().getLastJobDate(jobDetails1);
        String todayDateString = new GetDate().getDate();

        Date todayDate = new SimpleDateFormat("yyyy-MM-dd").parse(todayDateString);
        logger.info("Dates " + todayDateString + " & " + lastJobDateString);
        String s = imdbConfig.getBaseUrl() + lastJobDateString + "," + todayDateString + "&" + imdbConfig.getEndUrl();
        addNewMovieDetails(s, jobDetails, todayDate);
    }

    public AdvancedSearchData callIMDBRestAPI(String url) {
        logger.info("IMDB API call at " + LocalDateTime.now());
        logger.info(url);
        ResponseEntity<AdvancedSearchData> data = restTemplate.getForEntity(url, AdvancedSearchData.class);
        if (data.getStatusCode() != HttpStatus.OK) {
            logger.error("IMDB Api call has failed with status code " + data.getStatusCode());
            throw new RuntimeException();
        }
        return data.getBody();
    }

    public void addNewMovieDetails(String s, Optional<JobDetails> jobDetails, Date todayDate) throws DuplicateKeyException {

        logger.info("IMDBService starting_____");
        AdvancedSearchData data = callIMDBRestAPI(s);

        if (data == null)
            throw new RuntimeException();

        jobDetailsRepository.save(new JobDetails(1L,new Date()));

        jobDetailsRepository.save(new JobDetails(todayDate));

        List<AdvancedSearchResult> resultList = data.results;

        for (AdvancedSearchResult result : resultList) {
            if (checkIfNotMovie(result))
                continue;
            IMDBMovieEntity movie = AdvancedSearchResultToMovieEntity(result);

            if (todayDate != null)
                movie.setReceivedDate(todayDate);

            Optional<String> imdbId = imdbMovieEntityRepository.findBycheckId(result.getId());

            if (imdbId.isPresent()) {
                logger.info("Rejecting the record as it is already present in DB");
                continue;
            }
            imdbMovieEntityRepository.save(movie);
            logger.info(movie.getTitle());
        }
    }

    private boolean checkIfNotMovie(AdvancedSearchResult result) {
        if (result.getRuntimeStr() == null || ((Integer.parseInt(result.getRuntimeStr().split(" ")[0]) < 30) && result.getRuntimeStr().split(" ")[1].equals("min"))) {
            return true;
        }
        return false;
    }

    private IMDBMovieEntity AdvancedSearchResultToMovieEntity(AdvancedSearchResult result) {
        IMDBMovieEntity movie = new IMDBMovieEntity(result.getId());
        movie.setTitle(result.getTitle());
        movie.setDescription(result.getDescription());
        movie.setGenres(result.getGenres());
        movie.setImage(result.getImage());
        movie.setRuntimeStr(result.getRuntimeStr());
        movie.setIMDbRating(result.getImDbRating());
        movie.setOttAvailable(false);
        movie.setLastChecked(new Date());
        movie.setIMDbRatingVotes(result.getImDbRatingVotes());
        return movie;
    }
}
