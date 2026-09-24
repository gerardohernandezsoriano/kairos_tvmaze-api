package com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;

public record CommentResponse(
        String comment,
        Integer rating
) {

    public static CommentResponse fromDomain(Comment comment) {
        return new CommentResponse(
                comment.comment(),
                comment.rating()
        );
    }
}
