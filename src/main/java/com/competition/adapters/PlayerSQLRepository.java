package com.competition.adapters;

import com.competition.player.Player;
import com.competition.player.PlayerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

interface PlayerSQLRepository extends PlayerRepository, JpaRepository<Player,String>
{
}
