package com.competition.player;

import com.competition.club.ClubDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
@CrossOrigin
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerDTO player, @RequestBody ClubDTO club) {
        return playerService.createPlayer(player, club);
    }
    @GetMapping("/")
    public  ResponseEntity<?> getAll(){
        return playerService.getAll();
    }



}
