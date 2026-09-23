package com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze;

import com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze.dto.TVMazeShowResponse;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import java.util.Arrays;
import java.util.List;

@Component
public class TVMazeClientAdapter implements TVMazeClient {

    private final RestClient restClient;

    public TVMazeClientAdapter(RestClient restClient){
        this.restClient= restClient;
    }

    @Override
    public List<Show> searchShows(String query) {

        TVMazeShowResponse[] response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/shows")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .body(TVMazeShowResponse[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.stream(response)
                .map(this::toDomain)
                .toList();
    }


    private Show toDomain(TVMazeShowResponse tvMazeShow) {

        String channel = resolveChannel (tvMazeShow);

        return new Show(
                tvMazeShow.id(),
                tvMazeShow.name(),
                channel,
                tvMazeShow.summary(),
                tvMazeShow.genres()
        );
    }

    private String resolveChannel(TVMazeShowResponse show) {

        if (show.network() != null && show.network().name() != null) {
            return show.network().name();
        }

        if (show.webChannel() != null && show.webChannel().name() != null) {
            return show.webChannel().name();
        }

        return null;
    }
}
