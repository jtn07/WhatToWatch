package com.example.whattowatchbeta.OTT;

import com.example.whattowatchbeta.IMDB.IMDBMovieEntityRepository;
import com.example.whattowatchbeta.IMDB.Model.IMDBMovieEntity;
import com.example.whattowatchbeta.OTT.StreamingModels.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TMDBService {

    @Autowired
    private NewDetailsRepository newDetailsRepository;
    @Autowired
    private StreamingInfoRepository streamingInfoRepository;

    @Autowired
    private IMDBMovieEntityRepository imdbMovieEntityRepository;
    private final ObjectMapper mapper=new ObjectMapper();

    Logger logger= LoggerFactory.getLogger(TMDBService.class);

    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    TMDBConfig tmdbConfig;
    public void getOTTDetails(List<IMDBMovieEntity> imdbList ){



       imdbList.forEach(x->{
            String url = tmdbConfig.getStartUrl()+x.getId()+tmdbConfig.getEndUrl();

           ResponseEntity<String> response = null;
           try {

                 response = restTemplate.getForEntity(url, String.class);
               System.out.println(response.toString());
                
            } catch (RestClientException e) {
               logger.warn("Exception in getting information of "+x.getTitle());
               return;
            }

           JsonNode jsonNode;
           try {
               jsonNode = mapper.readTree(response.getBody());
           } catch (JsonProcessingException ignored) {
               logger.warn("Exception in json mapping ");
               return;
           }

           List<JsonNode> jsonNodeList = jsonNode.findValues("IN");
           List<InNode> inNodes = new ArrayList<>();
           jsonNodeList.forEach(X -> {
               System.out.println(X.toString());
               inNodes.add(new InNode(X.get("link").toString(),null,null));
           });

           if (inNodes.size() == 0) {
               logger.info("No streaming information available at this time for "+ x.getId());
               return;
           }

           saveStreamingInfo(x.getId(), inNodes); saveNewDetailsInfo(x, inNodes);
               updateIsOTTAvailableInMovieDB(x, new Date());

       });
    }

    private void updateIsOTTAvailableInMovieDB(IMDBMovieEntity x, Date date) {
        x.setOttAvailable(true);
        x.setLastChecked(date);
        imdbMovieEntityRepository.save(x);
    }

    private void saveStreamingInfo(String imdb, List<InNode> inNodeList){
        try {
            streamingInfoRepository.save(new StreamingInfo(imdb, inNodeList));
        } catch (Exception e) {
            logger.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +"StreamingInfo cannot be saved in StreamingInfo Table " + imdb);
            logger.info(String.valueOf(e));

        }

    }



    private void saveNewDetailsInfo(IMDBMovieEntity imdbMovieEntity, List<InNode> inNodes){

        List<NewDetailsNode> newDetailsNodeList = new ArrayList<>();
        try {
            inNodes
                    .forEach(newDetail -> newDetailsNodeList.add(new NewDetailsNode(newDetail.getLink())));
            newDetailsRepository.save(new NewDetails(imdbMovieEntity.getId(), imdbMovieEntity.getIMDbRating(),imdbMovieEntity.getTitle(),newDetailsNodeList,imdbMovieEntity.getImage()));
        } catch (Exception e) {
            logger.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +"Exception in saving data in new Details nodes ");
        }
    }

}
