package com.example.whattowatchbeta.Mail;


import com.example.whattowatchbeta.IMDB.IMDBMovieEntityRepository;

import com.example.whattowatchbeta.OTT.NewDetailsRepository;
import com.example.whattowatchbeta.OTT.StreamingModels.NewDetails;
import com.example.whattowatchbeta.OTT.StreamingModels.NewDetailsNode;
import com.example.whattowatchbeta.User.UserEntity;
import com.example.whattowatchbeta.User.UserService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailService {


    @Autowired
    private NewDetailsRepository newDetailsRepository;

    @Autowired
    IMDBMovieEntityRepository imdbMovieEntityRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private SendGridConfig sendGridConfig;

    @Scheduled(cron = "0 0 17 * * *",zone = "IST")
    public void getOTTDetails(){
       List<NewDetails> newDetails = newDetailsRepository.findAll();

        logger.info(newDetails.toString());
       // sendMail(newDetails);

        sendHTMLMail(newDetails);

    }
    @Autowired
    private Environment environment;
    Logger logger = LoggerFactory.getLogger(MailService.class);


    public void sendMail(List<NewDetails> newDetailsList) {
        logger.info("Weekly mail job started.....");
        Email from = new Email(sendGridConfig.getFrom());
        from.setName(sendGridConfig.getName());
        String subject = "This Week's new OTT releases";
        String api = environment.getProperty(sendGridConfig.getApiKey());

        String matter = makeOttDetailsContent(newDetailsList);

        List<UserEntity> userEntities= userService.getSubscribers();
        userEntities
                .forEach(x-> {
                    String bodyMatter= getGreetingsText(x.getName()) + matter;
                    Content content = new Content("text/plain", bodyMatter);

        Email to = new Email(x.getEmail());
        Mail mail = new Mail(from, subject, to, content);
        SendGrid sg = new SendGrid(api);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info(response.getStatusCode()+" for mail "+ x.getEmail());
        } catch (IOException e) {
           logger.warn("Cannot send mail to user "+ x.getEmail());
        }
                });
    }

    private String getGreetingsText(String name) {

        return "Hello!, "+ name + "\n" +
                " Enjoy these movies this weekend. \n \n \n";
    }

    private String makeOttDetailsContent(List<NewDetails> newDetailsList) {
        StringBuilder stringBuilder = new StringBuilder();
        newDetailsList.forEach(x -> {
            stringBuilder.append(x.getImdbId()).append("\n");
            List<NewDetailsNode> newDetailsNode =x.getNewDetailsNodes();
            newDetailsNode.forEach(y->{
                stringBuilder.append(y.getLink()).append("\n");
            });stringBuilder.append("\n");stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }


    public void sendHTMLMail(List<NewDetails> newDetailsList ) {

        logger.info("Weekly mail job started");
        Email from = new Email(sendGridConfig.getFrom());
        from.setName(sendGridConfig.getName());
        String subject = "This Week's new OTT releases";
        String api = sendGridConfig.getApiKey();

        String htmlMessage=new HTMLPage().getHTMLasString(newDetailsList);


        Content content = new Content("text/html",htmlMessage);
        List<UserEntity> userEntities= userService.getSubscribers();
        userEntities.forEach(x-> {
            Email to = new Email(x.getEmail());
            Mail mail = new Mail(from, subject, to, content);
            SendGrid sg = new SendGrid(api);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                logger.info(response.getStatusCode()+" for mail "+ x.getEmail());
                logger.info(response.getBody());
            } catch (IOException e) {
                logger.warn("Cannot send mail to user "+ x.getEmail());
            }
        });

    }
}
