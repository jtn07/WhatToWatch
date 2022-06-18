package com.example.whattowatchbeta.OTT;


import com.example.whattowatchbeta.IMDB.IMDBMovieEntityRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private TMDBService tmdbService;

    private final Logger logger= LoggerFactory.getLogger(OTTService.class);
    private final OTTConfig ottConfig;

    public OTTService( OTTConfig ottConfig) {
        this.ottConfig = ottConfig;
    }
    private final ObjectMapper mapper=new ObjectMapper();
    private final RestTemplate restTemplate=new RestTemplate();


    @Scheduled(cron = "0 0 12 * * FRI")
    public void scheduledOTTCheckService() {
        Date date = new Date();
       // Date previousDate = new Date(date.getTime());   //- (4 * ONE_DAY_MILLI_SECONDS)
        List<IMDBMovieEntity> imdbId = getMoviesForOTTCheck();
        List<IMDBMovieEntity> OTTFailedList = new ArrayList<>();
        imdbId.forEach(x -> {

            Boolean update = updateOTTAvailability(x.getId(),x);

            if (update) updateIsOTTAvailableInMovieDB(x, new Date());

            else OTTFailedList.add(x);

            try {
               Thread.sleep(6000);
            } catch (InterruptedException e) {
               logger.info(" Lol this is not expected \n"+ e);
            }
        });
        tmdbService.getOTTDetails(OTTFailedList);
    }

    public Boolean updateOTTAvailability(String imdbid,IMDBMovieEntity imdbottdao) {
        logger.info("OTT check service started....");
        String OTTAvailabilityCheckUrl = ottConfig.getOTTAvailabilityCheckUrl() + imdbid + "&output_language=en";
        String responseBody = "";
        HttpEntity<Object> requestEntity = new OTTHttpRequestGenerator().generateHttpEntity(ottConfig.getMap());

        try {
            logger.info("Calling ott service provider api for "+imdbid);
            ResponseEntity<String> response = restTemplate.exchange(OTTAvailabilityCheckUrl, HttpMethod.GET, requestEntity, String.class);
            responseBody = response.getBody();
            logger.info("Received response from OTT service provider api for "+imdbid);
        } catch (RestClientException e) {
            logger.info("Unable to retrieve streaming information for"+ imdbid);
            return false;
        }

        JsonNode jsonNode= null;
        try{
            jsonNode = mapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            logger.warn("Exception in parsing the response for movie id "+ imdbid);
            return false;
        }

        List<InNode> inNodes = new ArrayList<>();
        List<JsonNode> jsonNodeList = null;
        try {
            jsonNodeList = jsonNode.findValues("in");
        } catch (Exception e) {
            logger.warn("Exception in parsing the response for movie id "+ imdbid);
            return false;
        }
        logger.info("Parsing successful for "+imdbid);
        jsonNodeList.forEach(x -> inNodes.add(new InNode(x.get("link").toString(), x.get("added").toString(), x.get("leaving").toString())));

        if (inNodes.size() == 0) {
            logger.info("No streaming information available at this time for "+ imdbid);
            return false;
        }
        logger.info("Found "+inNodes.size()+" watch providers for "+imdbid);
        saveStreamingInfo(imdbid, inNodes);saveNewDetailsInfo(imdbid, imdbottdao, inNodes);

            updateIsOTTAvailableInMovieDB(imdbottdao,new Date());
            return true;

    }

    private void saveNewDetailsInfo(String imdbid, IMDBMovieEntity imdbottdao, List<InNode> inNodes) {

            List<NewDetailsNode> newDetailsNodeList = new ArrayList<>();
            try {
                inNodes
                        .forEach(x -> newDetailsNodeList.add(new NewDetailsNode(x.getLink())));
                newDetailsRepository.save(new NewDetails(imdbid, imdbottdao.getIMDbRating(),imdbottdao.getTitle(),newDetailsNodeList,imdbottdao.getImage()));
                logger.info("Saved newDetails info for "+ imdbid);
            } catch (Exception e) {
                logger.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +"Exception in saving data in new Details nodes ");
            }
    }

    @Scheduled(cron = "0 0 12 * * MON", zone = "IST")
    private void deleteNewDetails() {
        newDetailsRepository.deleteAll();
    }

    private  void saveStreamingInfo(String imdbid,List<InNode> inNodes){

        try {
            streamingInfoRepository.save(new StreamingInfo(imdbid, inNodes));
            logger.info("Saved StreamingInfo info for "+ imdbid);
        } catch (Exception e) {
            logger.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +"StreamingInfo cannot be saved in StreamingInfo Table " + imdbid);
            logger.info(String.valueOf(e));
        }
    }


    protected void updateIsOTTAvailableInMovieDB(IMDBMovieEntity imdbMovieEntity, Date date) {

        imdbMovieEntity.setOttAvailable(true);
        imdbMovieEntity.setLastChecked(date);
        try{

            imdbMovieEntityRepository.save(imdbMovieEntity);
        } catch (Exception e) {
           logger.warn("Cannot update the information in imdbmovieentity table for "+ imdbMovieEntity.getId());
        }

    }

    public List<IMDBMovieEntity> getMoviesForOTTCheck() {
        return imdbMovieEntityRepository.getMovieEntityByOTTAvailableFalse();
    }

}
