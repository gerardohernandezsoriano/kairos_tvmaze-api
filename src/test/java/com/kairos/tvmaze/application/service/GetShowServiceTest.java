package com.kairos.tvmaze.application.service;

import com.kairos.tvmaze.tvmaze_api.aplication.service.GetShowService;
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
public class GetShowServiceTest {
    @Mock
    private TVMazeClient tvMazeClient;

    private GetShowService getShowService;

    @BeforeEach
    void setUp() {
        getShowService = new GetShowService(tvMazeClient);
    }

    @Test
    void shouldReturnShowFromTvMazeClient() {

        // Arrange
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

        // Act
        Show result = getShowService.getShowById(showId);

        // Assert
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

        // Arrange
        Long showId = 1L;

        when(tvMazeClient.getShowById(showId))
                .thenThrow(
                        new ExternalServiceException(
                                "TVMaze service is unavailable"
                        )
                );

        // Act & Assert
        assertThatThrownBy(
                () -> getShowService.getShowById(showId)
        )
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("TVMaze service is unavailable");

        verify(tvMazeClient).getShowById(showId);

    }
}
