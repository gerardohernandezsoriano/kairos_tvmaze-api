package com.kairos.tvmaze.application.service;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowDetailResult;
import com.kairos.tvmaze.tvmaze_api.aplication.service.GetShowService;
import com.kairos.tvmaze.tvmaze_api.domain.exception.ExternalServiceException;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetShowServiceTest {

    @Mock
    private TVMazeClient tvMazeClient;

    @Mock
    private ShowCache showCache;

    @Mock
    private CommentRepository commentRepository;

    private GetShowService getShowService;

    @BeforeEach
    void setUp() {
        getShowService = new GetShowService(
                tvMazeClient,
                showCache,
                commentRepository
        );
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

        List<Comment> comments = List.of(
                new Comment(showId, "Great show", 5)
        );

        when(showCache.findById(showId))
                .thenReturn(Optional.empty());

        when(tvMazeClient.getShowById(showId))
                .thenReturn(expectedShow);

        when(commentRepository.findByShowId(showId))
                .thenReturn(comments);

        ShowDetailResult result = getShowService.getShowById(showId);

        assertThat(result).isNotNull();
        assertThat(result.show()).isEqualTo(expectedShow);
        assertThat(result.comments()).isEqualTo(comments);

        verify(tvMazeClient).getShowById(showId);
        verify(showCache).save(expectedShow);
        verify(commentRepository).findByShowId(showId);
    }

    @Test
    void shouldPropagateExternalServiceException() {

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
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("TVMaze service is unavailable");

        verify(tvMazeClient).getShowById(showId);

        verify(showCache, never())
                .save(any());

        verify(commentRepository, never())
                .findByShowId(any());
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

        List<Comment> comments = List.of(
                new Comment(showId, "Excellent", 5)
        );

        when(showCache.findById(showId))
                .thenReturn(Optional.of(cachedShow));

        when(commentRepository.findByShowId(showId))
                .thenReturn(comments);

        ShowDetailResult result = getShowService.getShowById(showId);

        assertThat(result.show())
                .isEqualTo(cachedShow);

        assertThat(result.comments())
                .isEqualTo(comments);

        verify(showCache).findById(showId);

        verify(tvMazeClient, never())
                .getShowById(showId);

        verify(showCache, never())
                .save(any());

        verify(commentRepository)
                .findByShowId(showId);
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

        List<Comment> comments = List.of(
                new Comment(showId, "Very good", 4)
        );

        when(showCache.findById(showId))
                .thenReturn(Optional.empty());

        when(tvMazeClient.getShowById(showId))
                .thenReturn(show);

        when(commentRepository.findByShowId(showId))
                .thenReturn(comments);

        ShowDetailResult result = getShowService.getShowById(showId);

        assertThat(result.show())
                .isEqualTo(show);

        assertThat(result.comments())
                .isEqualTo(comments);

        verify(showCache).findById(showId);
        verify(tvMazeClient).getShowById(showId);
        verify(showCache).save(show);
        verify(commentRepository).findByShowId(showId);
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

        verify(commentRepository, never())
                .findByShowId(any());
    }
}