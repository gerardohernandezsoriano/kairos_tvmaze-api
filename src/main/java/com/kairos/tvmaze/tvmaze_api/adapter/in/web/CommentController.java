package com.kairos.tvmaze.tvmaze_api.adapter.in.web;

import com.kairos.tvmaze.tvmaze_api.adapter.in.web.dto.CreateCommentRequest;
import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ICreateComment;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ICreateComment createComment;

    public CommentController(ICreateComment createCommentUseCase) {
        this.createComment = createCommentUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody CreateCommentRequest request
    ) {
        Comment comment = new Comment(
                request.showId(),
                request.comment(),
                request.rating()
        );

        createComment.create(comment);

        return ResponseEntity.ok().build();
    }
}
