package com.kairos.tvmaze.tvmaze_api.domain.exception;

public class ShowNotFoundException extends RuntimeException{
    public ShowNotFoundException(Long showId) {
        super("Show not found: " + showId);
    }
}
