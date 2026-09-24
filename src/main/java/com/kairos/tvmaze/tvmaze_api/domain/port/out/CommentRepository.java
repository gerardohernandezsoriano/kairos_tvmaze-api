package com.kairos.tvmaze.tvmaze_api.domain.port.out;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;

public interface CommentRepository {
    void save(Comment comment);
}