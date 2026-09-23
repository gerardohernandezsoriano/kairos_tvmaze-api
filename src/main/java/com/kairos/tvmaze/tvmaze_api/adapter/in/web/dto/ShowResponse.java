package com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto;

import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

import java.util.List;

public record ShowResponse(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres
) {

    public static ShowResponse fromDomain(Show show) {
        return new ShowResponse(
                show.id(),
                show.name(),
                show.channel(),
                show.summary(),
                show.genres()
        );
    }
}