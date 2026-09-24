package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.CommentDocument;
import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository.CommentMongoRepository;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Override
    public Map<Long, List<Comment>> findByShowIds(List<Long> showIds) {

        return repository.findByShowIdIn(showIds)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.groupingBy(Comment::showId));
    }

    private Comment toDomain(CommentDocument document) {
        return new Comment(
                document.getShowId(),
                document.getComment(),
                document.getRating()
        );
    }
    @Override
    public List<Comment> findByShowId(Long showId) {
        return repository.findByShowId(showId)
                .stream()
                .map(this::toDomain)
                .toList();
    }
}