package com.example.backend.dto;

import java.time.LocalDateTime;

public class TeamMessageDto {

    private Long id;
    private Long userId;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;

    public TeamMessageDto(Long id, Long userId, String authorName,
                          String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
