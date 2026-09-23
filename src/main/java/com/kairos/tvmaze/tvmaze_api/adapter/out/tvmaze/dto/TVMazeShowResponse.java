package com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze.dto;

import java.util.List;

public record TVMazeShowResponse(
        Long id,
        String name,
        String summary,
        List<String> genres,
        Network network,
        WebChannel webChannel
) {

    public record Network(
            String name
    ) {
    }

    public record WebChannel(
            String name
    ) {
    }
}
