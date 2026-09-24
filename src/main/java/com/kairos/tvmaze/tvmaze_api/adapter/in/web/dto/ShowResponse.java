package com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowSearchResult;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

import java.util.List;

public record ShowResponse(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres,
        List<CommentResponse> comments
) {

    public static ShowResponse fromDomain(Show show) {
        return new ShowResponse(
                show.id(),
                show.name(),
                show.channel(),
                show.summary(),
                show.genres(),
                List.of()
        );
    }

    public static ShowResponse fromSearchResult(
            ShowSearchResult result
    ) {
        return new ShowResponse(
                result.show().id(),
                result.show().name(),
                result.show().channel(),
                result.show().summary(),
                result.show().genres(),
                result.comments()
                        .stream()
                        .map(CommentResponse::fromDomain)
                        .toList()
        );
    }
}