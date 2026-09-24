package com.kairos.tvmaze.tvmaze_api.domain.model;

public record Comment(
        Long showId,
        String comment,
        Integer rating
) {
}