package com.kairos.tvmaze.tvmaze_api.adapter.out.mongodb.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "shows")
public class ShowDocument {

    @Id
    private Long id;

    private String name;

    private String channel;

    private String summary;

    private List<String> genres;

    @Indexed(
            name = "show_cache_ttl",
            expireAfter = "0s"
    )
    @Field("expiresAt")
    private Instant expiresAt;

    public ShowDocument() {
    }

    public ShowDocument(
            Long id,
            String name,
            String channel,
            String summary,
            List<String> genres,
            Instant expiresAt
    ) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.summary = summary;
        this.genres = genres;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}

