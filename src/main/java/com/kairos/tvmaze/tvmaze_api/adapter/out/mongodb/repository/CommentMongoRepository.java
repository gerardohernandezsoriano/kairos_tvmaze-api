package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentMongoRepository
        extends MongoRepository<CommentDocument, String> {
}