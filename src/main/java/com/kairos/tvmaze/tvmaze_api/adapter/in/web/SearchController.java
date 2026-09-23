package com.kairos.tvmaze.tvmaze_api.adapter.in.web;

import com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto.ShowResponse;
import com.kairos.tvmaze.tvmaze_api.domain.model.Show;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ISearchShows;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shows")
@Validated
public class SearchController {
    private final ISearchShows searchShows;

    public SearchController(ISearchShows searchShows) {
        this.searchShows = searchShows;
    }

    @GetMapping("/search")
    public List<ShowResponse> search(
            @RequestParam
            @NotBlank(message = "Query must not be blank")
            String query) {

        List<Show> shows = searchShows.search(query);

        return shows.stream()
                .map(ShowResponse::fromDomain)
                .toList();
    }
}
