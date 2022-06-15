package com.example.whattowatchbeta.OTT;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
public class OTTHttpRequestGenerator {


    public HttpEntity<Object> generateHttpEntity(Map<String,String>map){

        HttpHeaders headers=new HttpHeaders();
        List<String> keySet= new ArrayList<>(map.keySet());
        keySet.stream()
                .forEach(x-> {
                    headers.set(x,map
                            .get(x));
                });
        return new HttpEntity<Object>(headers);
    }
}
