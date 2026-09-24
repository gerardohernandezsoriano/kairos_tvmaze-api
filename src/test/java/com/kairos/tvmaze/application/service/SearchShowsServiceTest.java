package com.kairos.tvmaze.application.service;

import com.kairos.tvmaze.tvmaze_api.aplication.service.SearchShowsService;
import com.kairos.tvmaze.tvmaze_api.domain.exception.ExternalServiceException;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchShowsServiceTest {

    @Mock
    private TVMazeClient tvMazeClient;

    private SearchShowsService searchShowsService;

    @BeforeEach
    void setUp() {
        searchShowsService = new SearchShowsService(tvMazeClient);
    }

    @Test
    void shouldReturnShowsFromTvMazeClient() {
        String query = "batman";

        List<Show> expectedShows = List.of(
                new Show(
                        1L,
                        "Batman",
                        "ABC",
                        "A superhero show",
                        List.of("Action", "Drama")
                )
        );

        when(tvMazeClient.searchShows(query))
                .thenReturn(expectedShows);

        List<Show> result = searchShowsService.search(query);


        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(1L);
        assertThat(result.getFirst().name()).isEqualTo("Batman");
        assertThat(result.getFirst().channel()).isEqualTo("ABC");

        verify(tvMazeClient).searchShows(query);
    }

    @Test
    void shouldReturnEmptyListWhenNoShowsAreFound() {
        String query = "unknown";

        when(tvMazeClient.searchShows(query))
                .thenReturn(List.of());
        List<Show> result = searchShowsService.search(query);

        assertThat(result).isEmpty();

        verify(tvMazeClient).searchShows(query);
    }

    @Test
    void shouldPropagateExternalServiceException() {

        String query = "batman";

        when(tvMazeClient.searchShows(query))
                .thenThrow(new ExternalServiceException("TVMaze service is unavailable"));

        assertThatThrownBy(() -> searchShowsService.search(query))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("TVMaze service is unavailable");

        verify(tvMazeClient).searchShows(query);
    }
}
