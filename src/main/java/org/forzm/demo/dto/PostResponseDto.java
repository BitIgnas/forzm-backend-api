package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forzm.demo.model.PostType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private String id;
    private String title;
    private String content;
    private String contentMarkup;
    private String created;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    private String postImageUrl;
    private String forumForumName;
    private String forumImageUrl;
    private String userUsername;
    private String userProfileImageUrl;
    private Instant userDateCreated;
}

