package com.intuit.app.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.intuit.app.domain.Gender;
import com.intuit.app.domain.Player;
import com.intuit.app.service.PlayerService;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(controllers = PlayerResource.class)
public class PlayerResourceTest {

    @MockBean
    private PlayerService playerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenPlayersDataIsNotAttached() throws Exception {
        doNothing().when(playerService).uploadPlayers(any(MultipartFile.class));

        this.mockMvc.perform(post("/api/v1/players")
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void whenPlayersDataHasUnexpectedValues() throws Exception {
        doThrow(Exception.class).when(playerService).uploadPlayers(any(MultipartFile.class));

        MockMultipartFile mockMultipartFile =
            new MockMultipartFile("file", "test.csv", "multipart/form-data", "test data".getBytes());

        MockHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.multipart("/api/v1/players")
                .file(mockMultipartFile);

        this.mockMvc.perform(builder)
            .andDo(print())
            .andExpect(status().is5xxServerError());
    }

    @Test
    void whenPlayersDataIsAttached() throws Exception {
        doNothing().when(playerService).uploadPlayers(any(MultipartFile.class));

        MockMultipartFile mockMultipartFile =
            new MockMultipartFile("file", "test.csv", "multipart/form-data", "test data".getBytes());

        MockHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.multipart("/api/v1/players")
                .file(mockMultipartFile);

        this.mockMvc.perform(builder)
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    void whenPlayersDataIsRetrievedSuccessfully1() throws Exception {
        Player player1 = new Player(1L, "player1", Gender.MALE, "test");
        Player player2 = new Player(2L, "player2", Gender.MALE, "test");

        Page<Player> playersPage = new PageImpl<>(Arrays.asList(player1, player2));

        doReturn(playersPage).when(playerService).getAllPlayers(any(Pageable.class));

        this.mockMvc.perform(get("/api/v1/players")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("player1")));
    }

    //@Test
    //void playerCannotBeAddedWithoutName() throws Exception {
    //    when(playerRepository.save(any(Player.class)))
    //        .thenReturn(Player.builder().id(1L).name("chandu").build());
    //
    //    this.mockMvc.perform(post("/api/v1/players")
    //        .contentType(MediaType.APPLICATION_JSON)
    //        .content("{\"gender\": \"MALE\"}"))
    //        .andDo(print())
    //        .andExpect(status().isBadRequest());
    //}

    //@Test
    //void playerCreatedSuccessfully() throws Exception {
    //    when(playerRepository.save(any(Player.class)))
    //        .thenReturn(Player.builder().id(1L).name("chandu").build());
    //
    //    this.mockMvc.perform(post("/api/v1/players")
    //        .contentType(MediaType.APPLICATION_JSON)
    //        .content("{\"name\": \"chandu3\"}"))
    //        .andDo(print())
    //        .andExpect(status().isCreated())
    //        .andExpect(content().json("{\n"
    //            + "    \"id\": 1,\n"
    //            + "    \"name\": \"chandu\"\n"
    //            + "}"));
    //}
}
