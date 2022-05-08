package com.competition.mapping;

import com.competition.club.Club;
import com.competition.club.ClubDTO;

public class ClubMapping {

    public static Club map(ClubDTO c){
        return Club.builder()
                .name(c.getName().trim())
                .city(c.getCity().trim())
                .build();
    }
}
