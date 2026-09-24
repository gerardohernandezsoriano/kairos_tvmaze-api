package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.ShowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowMongoRepository
        extends MongoRepository<ShowDocument, Long> {
}
