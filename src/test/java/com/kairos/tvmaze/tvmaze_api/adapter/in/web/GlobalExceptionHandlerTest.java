package com.kairos.tvmaze.tvmaze_api.adapter.in.web;
import com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto.ErrorResponse;
import com.kairos.tvmaze.tvmaze_api.domain.exception.ExternalServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldReturn503WhenExternalServiceFails() {

        ExternalServiceException exception =
                new ExternalServiceException(
                        "TVMaze service is unavailable"
                );

        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/shows/search");

        var response =
                handler.handleExternalServiceException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);

        assertThat(response.getBody()).isNotNull();

        ErrorResponse body = response.getBody();

        assertThat(body.status()).isEqualTo(503);
        assertThat(body.error())
                .isEqualTo("Service Unavailable");
        assertThat(body.message())
                .isEqualTo("TVMaze service is unavailable");
        assertThat(body.path())
                .isEqualTo("/shows/search");
    }

    @Test
    void shouldReturn400WhenRequestParametersAreInvalid() {
        MockHttpServletRequest request =
                new MockHttpServletRequest();

        request.setRequestURI("/shows/search");

        org.springframework.web.method.annotation.HandlerMethodValidationException exception =
                org.mockito.Mockito.mock(
                        org.springframework.web.method.annotation.HandlerMethodValidationException.class
                );

        var response =
                handler.handleValidationException(
                        exception,
                        request
                );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(response.getBody()).isNotNull();

        ErrorResponse body = response.getBody();

        assertThat(body.status()).isEqualTo(400);
        assertThat(body.error())
                .isEqualTo("Bad Request");
        assertThat(body.message())
                .isEqualTo("Invalid request parameters");
        assertThat(body.path())
                .isEqualTo("/shows/search");
    }


}
