package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb;

import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document.ShowDocument;
import com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.repository.ShowMongoRepository;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.ShowCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class MongoShowCacheAdapter implements ShowCache {

    private final ShowMongoRepository repository;

    @Value("${tvmaze.cache.ttl-seconds}")
    private long ttlSeconds;

    public MongoShowCacheAdapter(
            ShowMongoRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Optional<Show> findById(Long showId) {

        return repository.findById(showId)
                .map(this::toDomain);
    }

    @Override
    public void save(Show show) {

        ShowDocument document = new ShowDocument(
                show.id(),
                show.name(),
                show.channel(),
                show.summary(),
                show.genres(),
                Instant.now().plusSeconds(ttlSeconds)
        );

        repository.save(document);
    }

    private Show toDomain(ShowDocument document) {

        return new Show(
                document.getId(),
                document.getName(),
                document.getChannel(),
                document.getSummary(),
                document.getGenres()
        );
    }
}
