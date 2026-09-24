package com.kairos.tvmaze.tvmaze_api.adapter.in.web;
import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowDetailResult;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.IGetShow;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ISearchShows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISearchShows searchShows;

    @MockitoBean
    private IGetShow getShow;

    @Test
    void shouldReturnShowWithComments() throws Exception {

        Long showId = 1L;

        Show show = new Show(
                showId,
                "Batman",
                "ABC",
                "A superhero show",
                List.of("Action", "Drama")
        );

        List<Comment> comments = List.of(
                new Comment(showId, "Great show", 5),
                new Comment(showId, "Very good", 4)
        );

        ShowDetailResult result = new ShowDetailResult(
                show,
                comments
        );

        when(getShow.getShowById(showId))
                .thenReturn(result);

        mockMvc.perform(
                        get("/shows/{showId}", showId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Batman"))
                .andExpect(jsonPath("$.channel").value("ABC"))
                .andExpect(jsonPath("$.summary").value("A superhero show"))
                .andExpect(jsonPath("$.genres[0]").value("Action"))
                .andExpect(jsonPath("$.genres[1]").value("Drama"))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments.length()").value(2))
                .andExpect(jsonPath("$.comments[0].comment").value("Great show"))
                .andExpect(jsonPath("$.comments[0].rating").value(5))
                .andExpect(jsonPath("$.comments[1].comment").value("Very good"))
                .andExpect(jsonPath("$.comments[1].rating").value(4));
    }
}