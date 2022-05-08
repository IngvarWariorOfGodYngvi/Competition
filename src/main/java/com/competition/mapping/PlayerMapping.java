package com.competition.mapping;

import com.competition.player.Player;
import com.competition.player.PlayerDTO;

public class PlayerMapping {

   public static Player map(PlayerDTO p) {
        return Player.builder()
                .firstName(p.getFirstName())
                .secondName(p.getSecondName())
                .licenseNumber(p.getLicenseNumber())
                .vintage(p.getVintage())
                .build();

    }
}
