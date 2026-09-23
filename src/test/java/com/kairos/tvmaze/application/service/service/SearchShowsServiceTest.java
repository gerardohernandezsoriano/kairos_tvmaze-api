package com.kairos.tvmaze.application.service.service;

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
        // Arrange
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

        // Act
        List<Show> result = searchShowsService.search(query);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(1L);
        assertThat(result.getFirst().name()).isEqualTo("Batman");
        assertThat(result.getFirst().channel()).isEqualTo("ABC");

        verify(tvMazeClient).searchShows(query);
    }

    @Test
    void shouldReturnEmptyListWhenNoShowsAreFound() {
        // Arrange
        String query = "unknown";

        when(tvMazeClient.searchShows(query))
                .thenReturn(List.of());

        // Act
        List<Show> result = searchShowsService.search(query);

        // Assert
        assertThat(result).isEmpty();

        verify(tvMazeClient).searchShows(query);
    }

    @Test
    void shouldPropagateExternalServiceException() {
        // Arrange
        String query = "batman";

        when(tvMazeClient.searchShows(query))
                .thenThrow(new ExternalServiceException("TVMaze service is unavailable"));

        // Act & Assert
        assertThatThrownBy(() -> searchShowsService.search(query))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("TVMaze service is unavailable");

        verify(tvMazeClient).searchShows(query);
    }
}
