package com.competition.club;

import com.competition.mapping.ClubMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        if (clubRepository.existsById(uuid) && !clubRepository.existsByName(club.getName())) {
            Club one = clubRepository.getOne(uuid);
            one.changeCity(club.getCity());
            one.changeName(club.getName());
            clubRepository.save(one);
            return ResponseEntity.ok("Edytowano klubu");
        } else {
            return ResponseEntity.badRequest().body("Nieznaleziono klubu lub takowy już istnieje");
        }
    }

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(clubRepository.findAll());
    }

    public ResponseEntity<?> getAllClubNames() {
        List<String> list = new ArrayList<>();
        List<Club> all = clubRepository.findAll();
        all.sort(Comparator.comparing(Club::getName));

        all.forEach(e ->
                list.add(e.getName().trim())
        );
        return ResponseEntity.ok(list);
    }
}
