package com.kairos.tvmaze.tvmaze_api.domain.port.in;

import com.kairos.tvmaze.tvmaze_api.domain.model.Show;

public interface IGetShow {
    Show getShowById(Long showId);
}
