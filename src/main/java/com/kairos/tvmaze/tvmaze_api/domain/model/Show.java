package com.kairos.tvmaze.tvmaze_api.domain.model;

import java.util.List;

public record Show(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres
) {
}
