package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.CommentDocument;
import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository.CommentMongoRepository;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import org.springframework.stereotype.Component;

@Component
public class MongoCommentRepositoryAdapter implements CommentRepository {

    private final CommentMongoRepository repository;

    public MongoCommentRepositoryAdapter(
            CommentMongoRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(Comment comment) {

        CommentDocument document = new CommentDocument(
                null,
                comment.showId(),
                comment.comment(),
                comment.rating()
        );

        repository.save(document);
    }
}