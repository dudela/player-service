package com.intuit.app.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.intuit.app.domain.Player;
import com.intuit.app.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PlayerResource.class)
public class PlayerResourceTest {

    @MockBean
    private PlayerRepository playerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void playerCannotBeAssignedAnIdManually() throws Exception {
        when(playerRepository.save(any(Player.class)))
            .thenReturn(Player.builder().id(1L).name("chandu").build());

        this.mockMvc.perform(post("/api/v1/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\": \"1\", \"name\": \"chandu3\"}"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void playerCannotBeAddedWithoutName() throws Exception {
        when(playerRepository.save(any(Player.class)))
            .thenReturn(Player.builder().id(1L).name("chandu").build());

        this.mockMvc.perform(post("/api/v1/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"gender\": \"MALE\"}"))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void playerCreatedSuccessfully() throws Exception {
        when(playerRepository.save(any(Player.class)))
            .thenReturn(Player.builder().id(1L).name("chandu").build());

        this.mockMvc.perform(post("/api/v1/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"chandu3\"}"))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().json("{\n"
                + "    \"id\": 1,\n"
                + "    \"name\": \"chandu\"\n"
                + "}"));
    }
}
