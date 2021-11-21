package com.intuit.app.resource;

import com.intuit.app.domain.Player;
import com.intuit.app.repository.PlayerRepository;
import com.intuit.app.util.HeaderUtil;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PlayerResource {
    public static final String RESOURCE_NAME = "player";

    private final PlayerRepository playerRepository;

    public PlayerResource(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createEmployee(@RequestBody @Valid Player player) throws URISyntaxException {
        if(player.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil
                .createFailureAlert(RESOURCE_NAME, "id exists",
                    "ID cannot be assigned to " + RESOURCE_NAME)).body(null);
        }
        Player playerResource = playerRepository.save(player);

        return ResponseEntity.created(new URI("/api/v1/players/" + playerResource.getId()))
            .body(playerResource);
    }
}
