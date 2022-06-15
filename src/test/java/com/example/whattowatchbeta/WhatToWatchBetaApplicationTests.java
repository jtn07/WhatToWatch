package com.example.whattowatchbeta;

import com.example.whattowatchbeta.OTT.StreamingInfoRepository;
import com.example.whattowatchbeta.OTT.StreamingModels.InNode;
import com.example.whattowatchbeta.OTT.StreamingModels.StreamingInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WhatToWatchBetaApplicationTests {

    private final ObjectMapper mapper=new ObjectMapper();

    @Autowired
    private StreamingInfoRepository streamingInfoRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void OTTEntryTest() throws JsonProcessingException {
        String imdbid="tt13833978";
        String responseBody="{\"imdbID\":\"tt13833978\",\"tmdbID\":\"116799\",\"imdbRating\":77,\"imdbVoteCount\":19359,\"tmdbRating\":81,\"backdropPath\":\"/u2ekcvwO0AdSwUxFFhnLd9SV4Rh.jpg\",\"backdropURLs\":{\"1280\":\"https://image.tmdb.org/t/p/w1280/u2ekcvwO0AdSwUxFFhnLd9SV4Rh.jpg\",\"300\":\"https://image.tmdb.org/t/p/w300/u2ekcvwO0AdSwUxFFhnLd9SV4Rh.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/u2ekcvwO0AdSwUxFFhnLd9SV4Rh.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/u2ekcvwO0AdSwUxFFhnLd9SV4Rh.jpg\"},\"originalTitle\":\"The Lincoln Lawyer\",\"genres\":[80,18,9648],\"countries\":[\"US\"],\"year\":2022,\"firstAirYear\":2022,\"lastAirYear\":2022,\"episodeRuntimes\":[50],\"cast\":[\"Manuel Garcia-Rulfo\",\"Neve Campbell\",\"Becki Newton\",\"Jazz Raycole\",\"Angus Sampson\",\"Katrina Rosita\",\"Reggie Lee\"],\"significants\":[\"David E. Kelley\"],\"title\":\"The Lincoln Lawyer\",\"overview\":\"Sidelined after an accident, hotshot Los Angeles lawyer Mickey Haller restarts his career — and his trademark Lincoln — when he takes on a murder case.\",\"video\":\"au06yHMuMGc\",\"posterPath\":\"/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"posterURLs\":{\"154\":\"https://image.tmdb.org/t/p/w154/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"185\":\"https://image.tmdb.org/t/p/w185/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"342\":\"https://image.tmdb.org/t/p/w342/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"500\":\"https://image.tmdb.org/t/p/w500/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"780\":\"https://image.tmdb.org/t/p/w780/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"92\":\"https://image.tmdb.org/t/p/w92/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\",\"original\":\"https://image.tmdb.org/t/p/original/4jSaIqEU8CPBBNn4iVCK6wzPjSx.jpg\"},\"seasons\":1,\"episodes\":10,\"age\":15,\"status\":1,\"tagline\":\"Welcome to his office.\",\"streamingInfo\":{\"netflix\":{\"in\":{\"link\":\"https://www.netflix.com/title/81303831/\",\"added\":1653706779,\"leaving\":0}}},\"originalLanguage\":\"en\"}\n";
        JsonNode jsonNode= mapper.readTree(responseBody);
        List<InNode> inNodes=new ArrayList<>();
        List<JsonNode> jsonNodeList =jsonNode.findValues("in");
        jsonNodeList.forEach(x-> {
            inNodes.add(new InNode(x.get("link").toString(),x.get("added").toString(),x.get("leaving").toString()));
        });

        if(inNodes.size()==0)
            System.out.println("false");
        System.out.println(responseBody);
        try{
            StreamingInfo streamingInfo=new StreamingInfo(imdbid,inNodes);
            streamingInfoRepository.save(streamingInfo);
        } catch (Exception e) {
            System.out.println("trouble saving int the repository" );
        }
        System.out.println(true);
    }

}
