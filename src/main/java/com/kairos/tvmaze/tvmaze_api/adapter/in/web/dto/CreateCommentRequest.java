package com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto;

import jakarta.validation.constraints.*;

public record CreateCommentRequest(

        @NotNull(message = "Show ID must not be null")
        @Positive(message = "Show ID must be positive")
        Long showId,

        @NotBlank(message = "Comment must not be blank")
        String comment,

        @NotNull(message = "Rating must not be null")
        @Min(value = 0, message = "Rating must be at least 0")
        @Max(value = 5, message = "Rating must be at most 5")
        Integer rating
) {
}
