package com.kairos.tvmaze.application.service;

import com.kairos.tvmaze.tvmaze_api.aplication.service.GetShowService;
import com.kairos.tvmaze.tvmaze_api.domain.exception.ExternalServiceException;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.ShowCache;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class GetShowServiceTest {
    @Mock
    private TVMazeClient tvMazeClient;

    @Mock
    private ShowCache showCache;

    private GetShowService getShowService;

    @BeforeEach
    void setUp() {
        getShowService = new GetShowService(tvMazeClient,showCache);
    }

    @Test
    void shouldReturnShowFromTvMazeClient() {

        Long showId = 1L;

        Show expectedShow = new Show(
                showId,
                "Batman",
                "ABC",
                "A superhero show",
                List.of("Action", "Drama")
        );

        when(tvMazeClient.getShowById(showId))
                .thenReturn(expectedShow);

        Show result = getShowService.getShowById(showId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Batman");
        assertThat(result.channel()).isEqualTo("ABC");
        assertThat(result.summary())
                .isEqualTo("A superhero show");
        assertThat(result.genres())
                .containsExactly("Action", "Drama");

        verify(tvMazeClient).getShowById(showId);
    }

    @Test
    void shouldPropagateExternalServiceException() {
        Long showId = 1L;

        when(tvMazeClient.getShowById(showId))
                .thenThrow(
                        new ExternalServiceException(
                                "TVMaze service is unavailable"
                        )
                );

        assertThatThrownBy(
                () -> getShowService.getShowById(showId)
        )
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("TVMaze service is unavailable");

        verify(tvMazeClient).getShowById(showId);

    }

    @Test
    void shouldReturnShowFromCacheWhenItExists() {
        Long showId = 1L;

        Show cachedShow = new Show(
                showId,
                "Batman",
                "ABC",
                "A superhero show",
                List.of("Action", "Drama")
        );

        when(showCache.findById(showId))
                .thenReturn(Optional.of(cachedShow));

        Show result = getShowService.getShowById(showId);

        assertThat(result).isEqualTo(cachedShow);

        verify(showCache).findById(showId);

        verify(tvMazeClient, never())
                .getShowById(showId);

        verify(showCache, never())
                .save(any());
    }

    @Test
    void shouldGetShowFromTvMazeAndCacheItWhenItDoesNotExist() {

        Long showId = 1L;

        Show show = new Show(
                showId,
                "Batman",
                "ABC",
                "A superhero show",
                List.of("Action", "Drama")
        );

        when(showCache.findById(showId))
                .thenReturn(Optional.empty());

        when(tvMazeClient.getShowById(showId))
                .thenReturn(show);

        Show result = getShowService.getShowById(showId);

        assertThat(result).isEqualTo(show);

        verify(showCache).findById(showId);
        verify(tvMazeClient).getShowById(showId);
        verify(showCache).save(show);
    }

    @Test
    void shouldNotCacheWhenTvMazeFails() {


        Long showId = 1L;

        when(showCache.findById(showId))
                .thenReturn(Optional.empty());

        when(tvMazeClient.getShowById(showId))
                .thenThrow(
                        new ExternalServiceException(
                                "TVMaze service is unavailable"
                        )
                );

        assertThatThrownBy(
                () -> getShowService.getShowById(showId)
        )
                .isInstanceOf(ExternalServiceException.class);

        verify(showCache).findById(showId);
        verify(tvMazeClient).getShowById(showId);

        verify(showCache, never())
                .save(any());
    }
}
