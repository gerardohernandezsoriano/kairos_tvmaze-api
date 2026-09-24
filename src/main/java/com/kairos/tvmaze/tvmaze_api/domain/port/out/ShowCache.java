package com.kairos.tvmaze.tvmaze_api.domain.port.out;

import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

import java.util.Optional;

public interface ShowCache {
    Optional<Show> findById(Long showId);
    void save(Show show);

}
