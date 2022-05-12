package com.competition.player;

import java.util.List;

public interface PlayerRepository {

    List<Player> findAll();

    Player findByLicenseNumber(String licenseNumber);

    Player getOne(String uuid);

    boolean existsByLicenseNumber(String licenseNumber);

    Player save(Player entity);


    boolean existsById(String uuid);

    boolean existsByStartNumber(String startNumber);
}
