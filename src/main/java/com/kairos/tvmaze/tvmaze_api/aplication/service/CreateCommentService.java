package com.kairos.tvmaze.tvmaze_api.aplication.service;

import com.kairos.tvmaze.tvmaze_api.domain.model.Comment;
import com.kairos.tvmaze.tvmaze_api.domain.port.in.ICreateComment;
import com.kairos.tvmaze.tvmaze_api.domain.port.out.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateCommentService implements ICreateComment {

    private final CommentRepository commentRepository;

    public CreateCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void create(Comment comment) {
        commentRepository.save(comment);
    }
}
