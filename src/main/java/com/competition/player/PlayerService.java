package com.competition.player;

import com.competition.club.Club;
import com.competition.club.ClubDTO;
import com.competition.club.ClubRepository;
import com.competition.club.ClubService;
import com.competition.mapping.PlayerMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Objects;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ClubService clubService;
    private final ClubRepository clubRepository;

    public PlayerService(PlayerRepository playerRepository, ClubService clubService, ClubRepository clubRepository) {
        this.playerRepository = playerRepository;
        this.clubService = clubService;
        this.clubRepository = clubRepository;
    }

    public ResponseEntity<?> createPlayer(PlayerDTO player, ClubDTO club) {

        if (playerRepository.existsByLicenseNumber(player.getLicenseNumber()) && !player.getLicenseNumber().isEmpty()) {
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

    public ResponseEntity<?> updatePlayer(Player player, String club) {
        if (playerRepository.existsById(player.getUuid())) {
            Player one = playerRepository.getOne(player.getUuid());
            if (!one.getFirstName().equals(player.getFirstName()) && player.getFirstName() != null) {
                one.setFirstName(player.getFirstName());
            }
            if (!one.getSecondName().equals(player.getSecondName()) && player.getSecondName() != null) {
                one.setSecondName(player.getSecondName());
            }
            if (!one.getStartNumber().equals(player.getStartNumber()) && player.getStartNumber() != null) {
                if (playerRepository.existsByStartNumber(player.getStartNumber())) {
                    return ResponseEntity.badRequest().body("ktoś już ma taki numer startowy");
                } else {
                    one.setStartNumber(player.getStartNumber());
                }
            }
            if (!one.getLicenseNumber().equals(player.getLicenseNumber()) && player.getLicenseNumber() != null) {
                if (playerRepository.existsByLicenseNumber(player.getLicenseNumber())) {
                    return ResponseEntity.badRequest().body("ktoś juz ma taki numer Licencji");
                } else {
                    one.setLicenseNumber(player.getLicenseNumber());
                }
            }
            if (!one.getVintage().equals(player.getVintage()) && player.getVintage() != null) {
                one.setVintage(player.getVintage());
            }
            if (!one.getClub().getName().equals(club) && club != null && !club.equals("null")) {
                if (clubRepository.existsByName(club)) {
                    one.setClub(clubRepository.findByName(club));
                } else {
                    return ResponseEntity.badRequest().body("nie znaleziono klubu - zapis się nieudał");
                }
            }
            playerRepository.save(one);
            return ResponseEntity.ok("wprowadzono zmiany");
        } else {
            return ResponseEntity.badRequest().body("Niemożna znaleźć zawodnika");
        }

    }

    public String createPlayer(String[] data) {
        // Nazwisko, imię, rok urodzenia, numer licencji, klub, miasto
        String response = "";
        ClubDTO clubDTO = ClubDTO.builder()
                .name(data[4])
                .city(data[5]).build();
        PlayerDTO playerDTO = PlayerDTO.builder().secondName(data[0])
                .firstName(data[1])
                .vintage(data[2])
                .licenseNumber(data[3]).build();
        response = Objects.requireNonNull(createPlayer(playerDTO, clubDTO).getBody()).toString();
        return response;

    }

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(playerRepository.findAll().stream().sorted(Comparator.comparing(Player::getStartNumber).thenComparing(Player::getSecondName)));
    }
}
