package com.example.whattowatchbeta.OTT;


import com.example.whattowatchbeta.IMDB.IMDBMovieEntityRepository;
import com.example.whattowatchbeta.IMDB.IMDBMovieService;
import com.example.whattowatchbeta.IMDB.Model.IMDBMovieEntity;
import com.example.whattowatchbeta.OTT.StreamingModels.InNode;
import com.example.whattowatchbeta.OTT.StreamingModels.NewDetails;
import com.example.whattowatchbeta.OTT.StreamingModels.NewDetailsNode;
import com.example.whattowatchbeta.OTT.StreamingModels.StreamingInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class OTTService {
    private static final long ONE_DAY_MILLI_SECONDS = 24 * 60 * 60 * 1000;

    @Autowired
    private IMDBMovieEntityRepository imdbMovieEntityRepository;
    @Autowired
    private NewDetailsRepository newDetailsRepository;
    @Autowired
    private StreamingInfoRepository streamingInfoRepository;
    @Autowired
    private IMDBMovieService imdbMovieService;
    private final Logger logger= LoggerFactory.getLogger(OTTService.class);
    private final OTTConfig ottConfig;

    public OTTService( OTTConfig ottConfig) {
        this.ottConfig = ottConfig;
    }
    private final ObjectMapper mapper=new ObjectMapper();
    private final RestTemplate restTemplate=new RestTemplate();


    @Scheduled(cron = "0 0 12 * * FRI")
    public void scheduledOTTCheckService(){
        Date date=new Date();
       // Date previousDate = new Date(date.getTime());   //- (4 * ONE_DAY_MILLI_SECONDS)
        List<String> imdbId=getMoviesForOTTCheck();

        imdbId.forEach(x->{
            boolean update = false;
            try {
                update = updateOTTAvailability(x);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if(update)updateIsOTTAvailableInMovieDB(x);
            updateLastCheckInMovieDB(x,new Date());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(" Lol this is not expected");
            }
        });
    }

    public Boolean updateOTTAvailability(String imdbid) throws JsonProcessingException {
       String OTTAvailabilityCheckUrl = ottConfig.getOTTAvailabilityCheckUrl()+imdbid+"&output_language=en";
       String responseBody="";
        HttpEntity<Object> requestEntity= new OTTHttpRequestGenerator().generateHttpEntity(ottConfig.getMap());
        try {
            ResponseEntity<String>response = restTemplate.exchange(OTTAvailabilityCheckUrl, HttpMethod.GET, requestEntity, String.class);
            responseBody=response.getBody();
        } catch (RestClientException e) {
        logger.info("Exception in getting in streaming information");
            return false;
        }

        JsonNode jsonNode= mapper.readTree(responseBody);
        List<InNode> inNodes=new ArrayList<>();
        List<JsonNode> jsonNodeList =jsonNode.findValues("in");
        jsonNodeList.forEach(x-> {
            inNodes.add(new InNode(x.get("link").toString(),x.get("added").toString(),x.get("leaving").toString()));
        });

        if(inNodes.size()==0)
            return false;
        logger.info(responseBody);
        try{
            StreamingInfo streamingInfo=new StreamingInfo(imdbid,inNodes);
            streamingInfoRepository.save(streamingInfo);
        } catch (Exception e) {
            logger.warn("StreamingInfo cannot be saved in StreamingInfo"+ imdbid);
            logger.info(String.valueOf(e));
            return false;
        }

        List<NewDetailsNode> newDetailsNodeList =new ArrayList<>();
        try{
            inNodes
                    .forEach(x-> newDetailsNodeList.add(new NewDetailsNode(x.getLink())));
            newDetailsRepository.save(new NewDetails(imdbid,newDetailsNodeList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Scheduled(cron = "0 0 12 * * MON", zone = "IST")
    private void deleteNewDetails(){
        newDetailsRepository.deleteAll();
    }

    private void updateIsOTTAvailableInMovieDB(String imdbId){
        List<IMDBMovieEntity> imdbMovieEntity=  imdbMovieEntityRepository.getIMDBMovieEntitiesById(imdbId);
        imdbMovieEntity
                .forEach(x->x.setOttAvailable(true));
    }

    private void updateLastCheckInMovieDB(String imdbId,Date date){
        List<IMDBMovieEntity> imdbMovieEntity=  imdbMovieEntityRepository.getIMDBMovieEntitiesById(imdbId);
        imdbMovieEntity
                .forEach(x->x.setLastChecked(date));
    }

    public List<String> getMoviesForOTTCheck(){
        return imdbMovieEntityRepository.getMovieEntityByOTTAvailableFalse();
    }

}
