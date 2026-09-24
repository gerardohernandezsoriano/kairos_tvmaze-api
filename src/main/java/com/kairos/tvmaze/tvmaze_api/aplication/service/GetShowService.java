package com.kairos.tvmaze.tvmaze_api.aplication.service;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowDetailResult;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.IGetShow;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.ShowCache;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetShowService implements IGetShow {
    private final TVMazeClient tvMazeClient;
    private final ShowCache showCache;
    private final CommentRepository commentRepository;

    public GetShowService(
            TVMazeClient tvMazeClient,
            ShowCache showCache,
            CommentRepository commentRepository
    ) {
        this.tvMazeClient = tvMazeClient;
        this.showCache = showCache;
        this.commentRepository = commentRepository;
    }

    @Override
    public ShowDetailResult getShowById(Long showId) {

        Show show = showCache.findById(showId)
                .orElseGet(() -> getFromTvMazeAndCache(showId));

        List<Comment> comments = commentRepository.findByShowId(showId);

        return new ShowDetailResult(show, comments);
    }

    private Show getFromTvMazeAndCache(Long showId) {
        Show show = tvMazeClient.getShowById(showId);
        showCache.save(show);
        return show;
    }
}
