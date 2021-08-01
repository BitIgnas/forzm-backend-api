package org.forzm.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.forzm.demo.model.Forum;
import org.forzm.demo.model.PostType;
import org.forzm.demo.model.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    @NotNull
    private String title;
    @Lob
    @NotNull
    private String content;
    @NotNull
    @Enumerated(EnumType.STRING)
    @NotNull
    private PostType postType;
    @NotNull
    private String forumName;
}