package com.kairos.tvmaze.application.service;

import com.kairos.tvmaze.tvmaze_api.aplication.service.CreateCommentService;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CreateCommentServiceTest {

    private CommentRepository commentRepository;

    private CreateCommentService createCommentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);

        createCommentService = new CreateCommentService(
                commentRepository
        );
    }

    @Test
    void shouldSaveComment() {

        Comment comment = new Comment(
                1L,
                "Great show",
                5
        );

        createCommentService.create(comment);

        verify(commentRepository).save(comment);
    }
}
