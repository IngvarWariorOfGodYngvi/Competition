package com.competition.club;

import java.util.List;

public interface ClubRepository {

    List<Club> findAll();

    Club save(Club entity);

    Club getOne(String id);

    Club findByName(String name);

    boolean existsByName(String name);
    boolean existsById(String uuid);
}
