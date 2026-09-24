package com.kairos.tvmaze.tvmaze_api.domain.port.out;

import com.kairos.tvmaze.tvmaze_api.adapter.out.tvmaze.dto.TVMazeShowResponse;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

import java.util.List;

public interface TVMazeClient {
    List<Show> searchShows(String query);
    Show getShowById(Long showId);
}
