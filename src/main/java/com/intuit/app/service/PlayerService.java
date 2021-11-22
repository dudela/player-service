package com.intuit.app.service;

import com.intuit.app.domain.Player;
import com.intuit.app.repository.PlayerRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CSVFileUploadImpl csvFileUpload;

    public void uploadPlayers(final MultipartFile file) throws Exception {
        final List<Player> players = csvFileUpload.parseFile(file);
        playerRepository.saveAll(players);
    }

    public Page<Player> getAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    public Optional<Player> getPlayer(Long id) {
        return playerRepository.findById(id);
    }
}
