package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class CommentDocument {

    @Id
    private String id;

    @Indexed
    private Long showId;

    private String comment;

    private Integer rating;

    public CommentDocument() {
    }

    public CommentDocument(
            String id,
            Long showId,
            String comment,
            Integer rating
    ) {
        this.id = id;
        this.showId = showId;
        this.comment = comment;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}