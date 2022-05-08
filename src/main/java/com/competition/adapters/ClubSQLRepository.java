package com.competition.adapters;

import com.competition.club.Club;
import com.competition.club.ClubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

interface ClubSQLRepository extends ClubRepository, JpaRepository<Club,String>
{
}
