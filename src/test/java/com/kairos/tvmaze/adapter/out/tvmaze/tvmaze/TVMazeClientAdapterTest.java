package com.kairos.tvmaze.adapter.out.tvmaze.tvmaze;

import com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze.TVMazeClientAdapter;
import com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze.dto.TVMazeShowResponse;
import com.kairos.tvmaze.tvmaze_api.domain.exception.ExternalServiceException;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TVMazeClientAdapterTest {

    @Mock
    private RestClient restClient;


    @SuppressWarnings("rawtypes")
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private TVMazeClientAdapter tvMazeClientAdapter;

    @BeforeEach
    void setUp() {
        tvMazeClientAdapter = new TVMazeClientAdapter(restClient);
    }

    @Test
    void shouldMapNetworkNameToChannel() {
        TVMazeShowResponse show = new TVMazeShowResponse(
                1L,
                "Batman",
                "A superhero show",
                List.of("Action", "Drama"),
                new TVMazeShowResponse.Network("ABC"),
                null
        );

        TVMazeShowResponse[] response = {show};

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(ArgumentMatchers.<Function<UriBuilder,
                URI>>any()))
                .thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TVMazeShowResponse[].class)).thenReturn(response);


        List<Show> result = tvMazeClientAdapter.searchShows("batman");

        assertThat(result).hasSize(1);

        Show mappedShow = result.getFirst();

        assertThat(mappedShow.id()).isEqualTo(1L);
        assertThat(mappedShow.name()).isEqualTo("Batman");
        assertThat(mappedShow.channel()).isEqualTo("ABC");
        assertThat(mappedShow.summary()).isEqualTo("A superhero show");
        assertThat(mappedShow.genres())
                .containsExactly("Action", "Drama");
    }

    @Test
    void shouldUseWebChannelWhenNetworkIsNull() {
        TVMazeShowResponse show = new TVMazeShowResponse(
                2L,
                "The Office",
                "Comedy show",
                List.of("Comedy"),
                null,
                new TVMazeShowResponse.WebChannel("Netflix")
        );

        TVMazeShowResponse[] response = {show};

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(org.mockito.ArgumentMatchers.<java.util.function.Function<
                org.springframework.web.util.UriBuilder,
                java.net.URI>>any()))
                .thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TVMazeShowResponse[].class)).thenReturn(response);

        List<Show> result = tvMazeClientAdapter.searchShows("office");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().channel()).isEqualTo("Netflix");
    }

    @Test
    void shouldReturnEmptyListWhenTvMazeReturnsNull() {

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(org.mockito.ArgumentMatchers.<java.util.function.Function<
                org.springframework.web.util.UriBuilder,
                java.net.URI>>any()))
                .thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(TVMazeShowResponse[].class)).thenReturn(null);

        List<Show> result = tvMazeClientAdapter.searchShows("unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldTranslateRestClientExceptionToExternalServiceException() {
        when(restClient.get()).thenThrow(
                new RestClientException("Connection timeout")
        );

        assertThatThrownBy(
                () -> tvMazeClientAdapter.searchShows("batman")
        )
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("TVMaze service is unavailable");
    }
}