package com.kairos.tvmaze.application.service;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowSearchResult;
import com.kairos.tvmaze.tvmaze_api.aplication.service.SearchShowsService;
import com.kairos.tvmaze.tvmaze_api.domain.exception.ExternalServiceException;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchShowsServiceTest {

    @Mock
    private TVMazeClient tvMazeClient;
    @Mock
    private  CommentRepository commentRepository;

    private SearchShowsService searchShowsService;

    @BeforeEach
    void setUp() {
        searchShowsService = new SearchShowsService(tvMazeClient,commentRepository);
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

        when(commentRepository.findByShowIds(List.of(1L)))
                .thenReturn(Map.of());

        List<ShowSearchResult> result = searchShowsService.search(query);


        assertThat(result).hasSize(1);
        assertThat(result.getFirst().show().id()).isEqualTo(1L);
        assertThat(result.getFirst().show().name()).isEqualTo("Batman");
        assertThat(result.getFirst().show().channel()).isEqualTo("ABC");
        assertThat(result.getFirst().comments()).isEmpty();

        verify(tvMazeClient).searchShows(query);
        verify(commentRepository).findByShowIds(List.of(1L));
    }

    @Test
    void shouldReturnEmptyListWhenNoShowsAreFound() {
        String query = "unknown";

        when(tvMazeClient.searchShows(query))
                .thenReturn(List.of());
        List<ShowSearchResult> result = searchShowsService.search(query);

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
