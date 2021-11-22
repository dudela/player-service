package com.intuit.app.service;

import com.intuit.app.domain.Gender;
import com.intuit.app.domain.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class CSVFileUploadImpl implements FileUpload<Player> {

    @Override
    public List<Player> parseFile(MultipartFile file) throws Exception {
        final List<Player> players = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final Player player = new Player();
                    player.setName(data[0]);
                    player.setEmail(data[1]);
                    player.setGender(Gender.valueOf(data[2]));
                    players.add(player);
                }
                return players;
            }
        }
        catch (final ArrayIndexOutOfBoundsException | IOException e) {
            log.error("Failed to parse CSV file", e);
            throw new IOException("Failed to parse CSV file {}", e);
        }
    }
}
