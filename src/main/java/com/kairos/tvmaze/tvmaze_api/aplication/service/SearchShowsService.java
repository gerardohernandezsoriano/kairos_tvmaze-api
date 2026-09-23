package com.kairos.tvmaze.tvmaze_api.aplication.service;

import com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze.dto.TVMazeShowResponse;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ISearchShows;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SearchShowsService implements ISearchShows {

    private final TVMazeClient tvMazeClient;

    public SearchShowsService(TVMazeClient tvMazeClient) {
        this.tvMazeClient = tvMazeClient;
    }

    @Override
    public List<Show> search(String query) {
        return tvMazeClient.searchShows(query);
    }


}
