
package com.fpt.ruby.helper;

import com.fpt.ruby.business.constants.IntentConstants;
import com.fpt.ruby.business.model.MovieTicket;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ducna on 11/28/2014.
 */
public class MovieAnswerGenarator {
    private static final String UDF_ANS = "Chúng tôi không tìm thấy dữ liệu";

    public static String generateDynamicAnswer(List<MovieTicket> tickets, String intent){
        if (tickets.size() == 0){
            return UDF_ANS;
        }

        StringBuilder result = new StringBuilder();
        if (intent.equals(IntentConstants.CIN_NAME) || intent.equals(IntentConstants.MOV_TITLE)){
            HashSet<String> results = new HashSet<String>();
            if (intent.equals(IntentConstants.CIN_NAME)) tickets.stream().forEach(t->results.add(t.getCinema()));
            if (intent.equals(IntentConstants.MOV_TITLE)) tickets.stream().forEach(t->results.add(t.getMovie()));
            return String.join("</br>",results);
        }

        if (intent.equals(IntentConstants.MOV_DATE)){
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM hh:mm:ss a");
            HashSet<String> cinemas = new HashSet<String>();
            tickets.stream().forEach(t->cinemas.add(t.getCinema()));

            for (String cinema : cinemas){
                result.append(cinema + ":</br>");
                tickets.stream().filter(t->t.getCinema().equals(cinema)).collect(Collectors.groupingBy(MovieTicket::getMovie))
                        .forEach((m, lt) -> {
                            result.append("&emsp;-" + m + ": ");
                            lt.stream().forEach(t->result.append(sdf.format(t.getDate()) + "&emsp;"));
                            result.append("<br>");
                        });
                result.append("</br>");
            }
        }
        return result.toString();
    }

}
