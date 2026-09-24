package com.kairos.tvmaze.adapter.out.mongodb;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.MongoShowCacheAdapter;
import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.ShowDocument;
import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository.ShowMongoRepository;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoShowCacheAdapterTest {

    @Mock
    private ShowMongoRepository repository;

    private MongoShowCacheAdapter cacheAdapter;

    @BeforeEach
    void setUp() {
        cacheAdapter = new MongoShowCacheAdapter(repository);
    }

    @Test
    void shouldReturnShowWhenItExistsInCache() {

        ShowDocument document = new ShowDocument(
                1L,
                "Batman",
                "ABC",
                "A superhero show",
                List.of("Action", "Drama"),
                Instant.now().plusSeconds(3600)
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(document));

        Optional<Show> result =
                cacheAdapter.findById(1L);

        assertThat(result).isPresent();

        Show show = result.get();

        assertThat(show.id()).isEqualTo(1L);
        assertThat(show.name()).isEqualTo("Batman");
        assertThat(show.channel()).isEqualTo("ABC");
        assertThat(show.summary())
                .isEqualTo("A superhero show");
        assertThat(show.genres())
                .containsExactly("Action", "Drama");

        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenShowDoesNotExistInCache() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<Show> result =
                cacheAdapter.findById(999L);

        assertThat(result).isEmpty();

        verify(repository).findById(999L);
    }

    @Test
    void shouldSaveShowWithExpirationDate() {

        Show show = new Show(
                1L,
                "Batman",
                "ABC",
                "A superhero show",
                List.of("Action", "Drama")
        );

        ReflectionTestUtils.setField(
                cacheAdapter,
                "ttlSeconds",
                3600L
        );

        ArgumentCaptor<ShowDocument> captor =
                ArgumentCaptor.forClass(ShowDocument.class);

        cacheAdapter.save(show);

        verify(repository).save(captor.capture());

        ShowDocument savedDocument =
                captor.getValue();

        assertThat(savedDocument.getId())
                .isEqualTo(show.id());

        assertThat(savedDocument.getName())
                .isEqualTo(show.name());

        assertThat(savedDocument.getChannel())
                .isEqualTo(show.channel());

        assertThat(savedDocument.getSummary())
                .isEqualTo(show.summary());

        assertThat(savedDocument.getGenres())
                .containsExactlyElementsOf(show.genres());

        assertThat(savedDocument.getExpiresAt())
                .isAfter(Instant.now());
    }
}
