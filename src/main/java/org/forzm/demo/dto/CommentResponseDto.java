package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String content;
    private Instant dateReplied;
    private String postTitle;
    private Long postPostId;
    private String postPostType;
    private String postForumName;
    private String userUsername;
    private String userDateCreated;
    private String userProfileImageUrl;
}
