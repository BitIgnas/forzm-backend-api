package org.forzm.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class PostRequestDto {
    private String title;
    private String content;
    private String contentMarkup;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PostType postType;
    private String forumName;
}
