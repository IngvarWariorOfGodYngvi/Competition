package com.competition.club;

import com.competition.mapping.ClubMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public ResponseEntity<?> createClub(ClubDTO club) {
        if (clubRepository.existsByName(club.getName())) {
            return ResponseEntity.ok(clubRepository.findByName(club.getName()));
        } else {
            return ResponseEntity.ok(clubRepository.save(ClubMapping.map(club)));
        }
    }

    public Club createClub1(ClubDTO club) {

        if (clubRepository.existsByName(club.getName())) {
            return clubRepository.findByName(club.getName());
        } else {
            return clubRepository.save(ClubMapping.map(club));
        }

    }

    public ResponseEntity<?> updateClub(String uuid, ClubDTO club) {
        if (clubRepository.existsById(uuid)) {
            Club one = clubRepository.getOne(uuid);
            one.changeCity(club.getCity());
            one.changeName(club.getName());
            clubRepository.save(one);
            return ResponseEntity.ok("zmieniono nazwę klubu");
        } else {
            return ResponseEntity.badRequest().body("Nieznaleziono klubu");
        }
    }

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(clubRepository.findAll());
    }
}
