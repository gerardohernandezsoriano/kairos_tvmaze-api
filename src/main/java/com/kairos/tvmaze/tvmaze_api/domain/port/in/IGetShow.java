package com.kairos.tvmaze.tvmaze_api.domain.port.in;

import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowDetailResult;

public interface IGetShow {
    ShowDetailResult getShowById(Long showId);}
