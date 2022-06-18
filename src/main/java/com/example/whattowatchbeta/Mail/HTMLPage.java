package com.example.whattowatchbeta.Mail;

import com.example.whattowatchbeta.OTT.StreamingModels.NewDetails;

import java.util.List;

class HTMLPage {
    public String getHTMLasString(List<NewDetails> newDetailsList){
        String html="<html xmlns:th=\"http://www.thymeleaf.org\" >\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<head>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"+
                "    <title>This week's OTT Releases </title>\n" +
                "    <style>\n" +
                "    * {\n" +
                "    box-sizing: border-box;\n" +
                "    border-color: black;\n" +
                "        background-color: black;\n" +
                "    }\n" +
                "    body {\n" +
                "    font-family: Arial;\n" +
                "    font-size: 17px;\n" +
                "    }\n" +

                "  .content { "+
                " top: 20%;"+
                "left : 40%;"+
                "bottom: 10%;"+

                " };"+
                "\n" +
                "</style>"+
                "</head>\n" +
                "<body>\n" +
                "<div class=\"content\">"+
                "<h1>This week's OTT releases </h1>"   ;

        StringBuilder sb = new StringBuilder();

        newDetailsList.forEach(x->{
            String body="<a href="+x.getNewDetailsNodes().get(0).getLink()+">" +
                    "    <img alt=\"Qries\" src="+x.getImage() +
                    "         width=300\" height=\"250\">\n" +
                    "</a>\n" +
                    "<br>"    +
                    "Name: "+x.getTitle()+"   "+"IMDB Rating: "+x.getImdbRating()+
                    "<br> "+
                    "\n" +
                    "\n" ;
            sb.append(body);
        });


        String tail= "</div>"+
                "</body>" +
                "</html>";
        System.out.println(html+sb.toString()+tail);
        return html+sb.toString()+tail;
    }
}
