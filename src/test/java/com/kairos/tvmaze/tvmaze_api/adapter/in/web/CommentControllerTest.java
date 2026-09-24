package com.kairos.tvmaze.tvmaze_api.adapter.in.web;

import com.kairos.tvmaze.tvmaze_api.domain.port.in.ICreateComment;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICreateComment createComment;

    @Test
    void shouldCreateComment() throws Exception {

        String request = """
                {
                    "showId": 1,
                    "comment": "Great show",
                    "rating": 5
                }
                """;

        mockMvc.perform(
                        post("/comments")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(request)
                )
                .andExpect(status().isOk());

        verify(createComment).create(
                new com.kairos.tvmaze.tvmaze_api.domain.model.Comment(
                        1L,
                        "Great show",
                        5
                )
        );
    }

    @Test
    void shouldRejectBlankComment() throws Exception {

        String request = """
            {
                "showId": 1,
                "comment": "",
                "rating": 5
            }
            """;

        mockMvc.perform(
                        post("/comments")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldRejectRatingGreaterThanFive() throws Exception {

        String request = """
            {
                "showId": 1,
                "comment": "Great show",
                "rating": 6
            }
            """;

        mockMvc.perform(
                        post("/comments")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldRejectNegativeRating() throws Exception {

        String request = """
            {
                "showId": 1,
                "comment": "Great show",
                "rating": -1
            }
            """;

        mockMvc.perform(
                        post("/comments")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldRejectInvalidShowId() throws Exception {

        String request = """
            {
                "showId": 0,
                "comment": "Great show",
                "rating": 5
            }
            """;

        mockMvc.perform(
                        post("/comments")
                                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                                .content(request)
                )
                .andExpect(status().isBadRequest());
    }
}

