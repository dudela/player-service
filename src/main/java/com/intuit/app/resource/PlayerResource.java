package com.intuit.app.resource;

import com.intuit.app.domain.Player;
import com.intuit.app.service.PlayerService;
import com.intuit.app.util.PaginationUtil;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class PlayerResource {
    public static final String BASE_URI = "/api/v1/players";

    private final PlayerService playerService;

    public PlayerResource(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(value = "/players", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPlayers(@RequestParam(value = "file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Please input a valid file");
            }
            playerService.uploadPlayers(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully!");
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body("Please upload a valid file");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayers(Pageable pageable) {
        Page<Player> page = playerService.getAllPlayers(pageable);
        HttpHeaders headers =
            PaginationUtil.generatePaginationHttpHeaders(page, BASE_URI);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        Optional<Player> employee = playerService.getPlayer(id);
        return employee.map(emp -> ResponseEntity.ok().body(emp))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
