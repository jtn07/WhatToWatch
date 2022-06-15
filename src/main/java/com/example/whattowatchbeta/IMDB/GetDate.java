package com.example.whattowatchbeta.IMDB;

import com.example.whattowatchbeta.IMDB.Model.JobDetails;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Data
@Component
public class GetDate {
    @Autowired
    private JobDetailsRepository jobDetailsRepository;
    public String getDate() {
        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date date = new Date();
        formatDate.setTimeZone(TimeZone.getTimeZone("IST"));
        return formatDate.format(date);
    }
    public String getLastJobDate(Optional<JobDetails> jobDetails) throws ParseException {

        SimpleDateFormat formatDate = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date date =jobDetails.get().getDate();
        formatDate.setTimeZone(TimeZone.getTimeZone("IST"));
        return formatDate.format(date);
    }
}
