package com.kairos.tvmaze.tvmaze_api.domain.port.in;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;

public interface ICreateComment {
    void create(Comment comment);
}
