package com.kairos.tvmaze.tvmaze_api.aplication.service;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowSearchResult;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ISearchShows;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchShowsService implements ISearchShows {

    private final TVMazeClient tvMazeClient;
    private final CommentRepository commentRepository;

    public SearchShowsService(
            TVMazeClient tvMazeClient,
            CommentRepository commentRepository
    ) {
        this.tvMazeClient = tvMazeClient;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<ShowSearchResult> search(String query) {

        List<Show> shows = tvMazeClient.searchShows(query);

        if (shows.isEmpty()) {
            return List.of();
        }

        List<Long> showIds = shows.stream()
                .map(Show::id)
                .toList();

        Map<Long, List<Comment>> commentsByShowId =
                commentRepository.findByShowIds(showIds);

        return shows.stream()
                .map(show -> new ShowSearchResult(
                        show,
                        commentsByShowId.getOrDefault(show.id(), List.of())
                ))
                .toList();
    }

}
