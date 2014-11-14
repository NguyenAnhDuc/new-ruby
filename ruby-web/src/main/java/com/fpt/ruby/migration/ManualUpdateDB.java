package com.fpt.ruby.migration;

import com.fpt.ruby.business.model.NameMapper;
import com.fpt.ruby.business.model.TVProgram;
import com.fpt.ruby.business.service.NameMapperService;
import com.fpt.ruby.business.service.TVProgramService;

import java.util.List;

/**
 * Created by quang on 11/14/2014.
 */
public class ManualUpdateDB {



    public static void lowercaseTVProgramCollection(){
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

    public static void removeNameMapper(){
        NameMapperService nameMapperService = new NameMapperService();
        List<NameMapper> nameMappers =  nameMapperService.getAll();
        System.out.println(nameMappers.size());
        for (NameMapper nameMapper : nameMappers){
            if (nameMapper.getName().toLowerCase().equals("bóng đá") || nameMapper.getName().toLowerCase().equals("bong da")
                    || nameMapper.getName().toLowerCase().equals("phim") || nameMapper.getName().toLowerCase().equals("phim")){
                nameMapperService.remove(nameMapper);
            }
        }
        System.out.println("DONE");
    }

    public static void main(String[] args){
        //lowercaseTVProgramCollection();
        removeNameMapper();
    }
}
