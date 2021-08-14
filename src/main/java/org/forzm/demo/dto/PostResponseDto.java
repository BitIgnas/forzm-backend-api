package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forzm.demo.model.PostType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private String id;
    private String title;
    private String content;
    private String created;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    private String forumForumName;
    private String forumImageUrl;
    private String userUsername;
    private Instant userDateCreated;
    private String userProfileImageUrl;
}
