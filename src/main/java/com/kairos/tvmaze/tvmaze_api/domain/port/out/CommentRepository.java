package com.kairos.tvmaze.tvmaze_api.domain.port.out;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

    void save(Comment comment);

    Map<Long, List<Comment>> findByShowIds(List<Long> showIds);
}