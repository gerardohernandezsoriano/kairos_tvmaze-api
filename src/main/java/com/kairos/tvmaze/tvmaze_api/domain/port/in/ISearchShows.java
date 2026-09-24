package com.kairos.tvmaze.tvmaze_api.domain.port.in;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowSearchResult;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

import java.util.List;

public interface ISearchShows {
    List<ShowSearchResult> search(String query);
}
