package com.competition.club;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@CrossOrigin
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createCLub(@RequestBody ClubDTO club) {
        return clubService.createClub(club);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        return clubService.getAll();
    }

    @GetMapping("/names")
    public ResponseEntity<?> getAllClubNames() {
        return clubService.getAllClubNames();
    }

    @PutMapping("/")
    public ResponseEntity<?> updateClub(@RequestParam String uuid, @RequestBody ClubDTO club){
        return clubService.updateClub(uuid,club);
    }
}
