package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CommentMongoRepository
        extends MongoRepository<CommentDocument, String> {

    List<CommentDocument> findByShowIdIn(List<Long> showIds);
    List<CommentDocument> findByShowId(Long showId);
}