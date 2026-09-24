package com.kairos.tvmaze.tvmaze_api.aplication.service;

import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.IGetShow;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.springframework.stereotype.Service;

@Service
public class GetShowService implements IGetShow {

    private final TVMazeClient tvMazeClient;

    public GetShowService(TVMazeClient tvMazeClient) {
        this.tvMazeClient = tvMazeClient;
    }

    @Override
    public Show getShowById(Long showId) {
        return tvMazeClient.getShowById(showId);
    }
}
