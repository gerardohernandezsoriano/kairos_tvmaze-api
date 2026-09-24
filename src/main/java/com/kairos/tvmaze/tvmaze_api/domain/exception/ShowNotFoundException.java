package com.kairos.tvmaze.tvmaze_api.domain.exception;

public class ShowNotFoundException extends RuntimeException{
    public ShowNotFoundException(String showId) {
        super("Show not found: " + showId);
    }
}
