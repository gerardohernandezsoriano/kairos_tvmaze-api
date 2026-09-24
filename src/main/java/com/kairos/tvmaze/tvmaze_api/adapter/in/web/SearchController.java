package com.kairos.tvmaze.tvmaze_api.adapter.in.web;

import com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto.ShowResponse;
import com.kairos.tvmaze.tvmaze_api.aplication.model.ShowSearchResult;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.IGetShow;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ISearchShows;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
@Validated
public class SearchController {
    private final ISearchShows searchShows;
    private final IGetShow getShow;

    public SearchController(ISearchShows searchShows, IGetShow getShow) {
        this.searchShows = searchShows;
        this.getShow = getShow;
    }

    @GetMapping("/search")
    public List<ShowResponse> search(
            @RequestParam
            @NotBlank(message = "Query must not be blank")
            String query) {

        return searchShows.search(query)
                .stream()
                .map(ShowResponse::fromSearchResult)
                .toList();
    }

    @GetMapping("/{showId}")
    public ShowResponse getShow(
            @PathVariable
            @Positive(message = "Show ID must be positive")
            Long showId) {

        Show show = getShow.getShowById(showId);

        return ShowResponse.fromDomain(show);
    }
}
