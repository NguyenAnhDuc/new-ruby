package com.fpt.ruby.migration;

import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.service.TVProgramService;

import java.util.List;

/**
 * Created by quang on 11/14/2014.
 */
public class LowerCaseTVProgram {
    public static void main(String[] args){
        TVProgramService tvProgramService = new TVProgramService();
        List<TVProgram> tvPrograms = tvProgramService.findAll();
        for (TVProgram tvProgram : tvPrograms){
            tvProgram.setChannel(tvProgram.getChannel().toLowerCase());
            tvProgram.setDescription(tvProgram.getDescription().toLowerCase());
            tvProgram.setType(tvProgram.getType().toLowerCase());
            tvProgram.setTitle(tvProgram.getTitle().toLowerCase());
            tvProgramService.save(tvProgram);
        }
        System.out.println("DONE");
    }
}
