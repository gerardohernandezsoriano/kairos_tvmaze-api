package com.kairos.tvmaze.tvmaze_api.aplication.model;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

import java.util.List;

public record ShowDetailResult(
        Show show,
        List<Comment> comments
) {
}
