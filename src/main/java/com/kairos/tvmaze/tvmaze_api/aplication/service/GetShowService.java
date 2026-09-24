package com.kairos.tvmaze.tvmaze_api.aplication.service;

import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.IGetShow;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.ShowCache;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.TVMazeClient;
import org.springframework.stereotype.Service;

@Service
public class GetShowService implements IGetShow {
    private final TVMazeClient tvMazeClient;
    private final ShowCache showCache;

    public GetShowService(
            TVMazeClient tvMazeClient,
            ShowCache showCache
    ) {
        this.tvMazeClient = tvMazeClient;
        this.showCache = showCache;

    }

    @Override
    public Show getShowById(Long showId) {

        return showCache.findById(showId)
                .orElseGet(() -> getFromTvMazeAndCache(showId));
    }

    private Show getFromTvMazeAndCache(Long showId) {

        Show show = tvMazeClient.getShowById(showId);

        showCache.save(show);

        return show;
    }
}
