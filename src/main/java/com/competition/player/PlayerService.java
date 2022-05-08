package com.competition.player;

import com.competition.club.Club;
import com.competition.club.ClubDTO;
import com.competition.club.ClubService;
import com.competition.mapping.PlayerMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ClubService clubService;

    public PlayerService(PlayerRepository playerRepository, ClubService clubService) {
        this.playerRepository = playerRepository;
        this.clubService = clubService;
    }

    public ResponseEntity<?> createPlayer(PlayerDTO player, ClubDTO club) {

        if (playerRepository.existsByLicenseNumber(player.getLicenseNumber())) {
            return ResponseEntity.badRequest().body("Zawodnik " + player.getSecondName() + " " + player.getFirstName() + " znajduje się już na liście");
        }

        Player pl = PlayerMapping.map(player);

        Club cl = clubService.createClub1(club);

        pl.setClub(cl);
        // posortuj wszytkich po klubie potem po nazwisku a potem nadaj numer starowy
//        pl.setStartNumber(/*max*/);

        playerRepository.save(pl);
        return ResponseEntity.ok("dodano Zawodnika " + player.getSecondName() + " " + player.getFirstName());
    }

    public ResponseEntity<?> updatePlayer(PlayerDTO player) {
        return null;
    }

    public String createPlayer(String[] data) {
        // Nazwisko, imię, rok urodzenia, numer licencji, klub, miasto
        String response ="";
        ClubDTO clubDTO = ClubDTO.builder()
                .name(data[4])
                .city(data[5]).build();
        PlayerDTO playerDTO = PlayerDTO.builder().secondName(data[0])
                .firstName(data[1])
                .vintage(data[2])
                .licenseNumber(data[3]).build();
        response =createPlayer(playerDTO, clubDTO).getBody().toString();
        return response;

    }

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(playerRepository.findAll().stream().sorted(Comparator.comparing(Player::getStartNumber)));
    }
}
