package com.metacoding.restserver.post;

import com.metacoding.restserver.user.User;
import lombok.Data;

import java.time.format.DateTimeFormatter;

public class PostResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;
        private String createdAt;
        private Integer userId;
        private String username;

        public DTO(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            this.userId = post.getUser().getId();
            this.username = post.getUser().getUsername();
        }
    }

    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String content;
        private String createdAt;
        private Integer userId;
        private String username;

        public ListDTO(Post post, User user) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            this.userId = user.getId();
            this.username = user.getUsername();
        }
    }
}